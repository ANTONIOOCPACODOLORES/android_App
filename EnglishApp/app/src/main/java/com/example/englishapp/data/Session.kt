package com.example.englishapp.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Usuario actual + progreso por lección + estadísticas. Elige Firebase si hay google-services.json, si no, local. */
object Session {
    const val PASS_PERCENT = 80

    /** Tope por visita a una pantalla, para no sumar horas si el teléfono se queda abierto. */
    private const val MAX_VISIT_SECONDS = 30 * 60L

    private lateinit var backend: Backend
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** Solo guardamos estadísticas cuando ya se cargaron las del usuario, para no pisarlas con ceros. */
    private var statsLoaded = false

    var isCloud by mutableStateOf(false)
        private set
    var user by mutableStateOf<UserProfile?>(null)
        private set
    val progress = mutableStateMapOf<Int, LessonProgress>()
    var stats by mutableStateOf(UserStats())
        private set

    /** Total de elementos que cuentan para el progreso (lecciones + temas de vocabulario). */
    val totalItems: Int get() = ProgressIds.size

    /** % general = promedio del mejor resultado de cada lección y de cada tema de vocabulario. */
    val overall: Int get() = ProgressIds.sumOf { progress[it]?.best ?: 0 } / ProgressIds.size
    val completed: Int get() = ProgressIds.count { (progress[it]?.best ?: 0) >= PASS_PERCENT }

    // ---- Lo de hoy (se reinicia solo al cambiar el día) ----
    val todayLessons: Int get() = if (stats.todayDate == dayString()) stats.todayLessons else 0
    val todayPractices: Int get() = if (stats.todayDate == dayString()) stats.todayPractices else 0
    val todaySeconds: Long get() = if (stats.todayDate == dayString()) stats.todaySeconds else 0L

    /** Racha vigente: solo cuenta si estudiaste hoy o ayer. */
    val streakShown: Int
        get() = if (stats.lastDay == dayString() || stats.lastDay == dayString(-1)) stats.streak else 0

    fun init(context: Context) {
        isCloud = FirebaseApp.getApps(context).isNotEmpty()
        backend = if (isCloud) FirebaseBackend() else LocalBackend(context)
        user = backend.currentUser()
    }

    suspend fun loadProgress() {
        try {
            val loaded = backend.loadProgress()
            progress.clear()
            progress.putAll(loaded)
        } catch (_: Exception) { /* sin conexión: se queda con lo que haya */ }
        try {
            stats = backend.loadStats()
            statsLoaded = true
        } catch (_: Exception) { /* sin conexión: no guardamos estadísticas hasta poder cargarlas */ }
    }

    /** Crea la cuenta pero NO deja la sesión abierta: el usuario debe iniciar sesión desde el login. */
    suspend fun register(name: String, email: String, pass: String): String? {
        val err = backend.register(name, email, pass)
        if (err == null) backend.logout()
        return err
    }

    suspend fun login(email: String, pass: String): String? {
        val err = backend.login(email, pass)
        if (err == null) {
            user = backend.currentUser()
            withTimeoutOrNull(5000) { loadProgress() }
        }
        return err
    }

    fun logout() {
        backend.logout()
        user = null
        progress.clear()
        stats = UserStats()
        statsLoaded = false
    }

    suspend fun saveLessonResult(lessonId: Int, correct: Int, total: Int) {
        if (total == 0) return
        val pct = correct * 100 / total
        val old = progress[lessonId] ?: LessonProgress()
        val updated = LessonProgress(maxOf(old.best, pct), pct, old.attempts + 1)
        progress[lessonId] = updated
        // Cuenta para la meta de hoy ("1 lección").
        updateStats { withStreak(it).copy(todayLessons = it.todayLessons + 1) }
        try { backend.saveProgress(lessonId, updated) } catch (_: Exception) { }
    }

    // ---------------- Estadísticas ----------------

    /** Práctica libre terminada (cuenta para la meta de hoy: "1 práctica"). */
    fun recordPractice() {
        updateStats { withStreak(it).copy(todayPractices = it.todayPractices + 1) }
    }

    /** Suma tiempo de estudio (lo llama TrackStudyTime al salir de una pantalla de estudio). */
    fun addStudySeconds(seconds: Long) {
        val s = seconds.coerceIn(0L, MAX_VISIT_SECONDS)
        if (s <= 0L) return
        updateStats { withStreak(it).copy(studySeconds = it.studySeconds + s, todaySeconds = it.todaySeconds + s) }
    }

    fun setWordLearned(key: String, learned: Boolean) {
        updateStats { it.copy(words = if (learned) it.words + key else it.words - key) }
    }

    /** Marca una palabra como aprendida (si ya lo estaba no hace nada). */
    fun markWordLearned(key: String) {
        if (key !in stats.words) setWordLearned(key, true)
    }

    fun setBanner(index: Int) {
        updateStats { it.copy(banner = index) }
    }

    // ---------------- Internos ----------------

    private fun updateStats(change: (UserStats) -> UserStats) {
        if (user == null) return
        val today = dayString()
        var s = stats
        if (s.todayDate != today) {
            s = s.copy(todayDate = today, todayLessons = 0, todayPractices = 0, todaySeconds = 0L)
        }
        s = change(s)
        stats = s
        if (statsLoaded) {
            scope.launch {
                try { backend.saveStats(s) } catch (_: Exception) { }
            }
        }
    }

    /** Primera actividad del día: suma a la racha si ayer también estudió, si no la reinicia en 1. */
    private fun withStreak(s: UserStats): UserStats {
        val today = dayString()
        if (s.lastDay == today) return s
        val next = if (s.lastDay == dayString(-1)) s.streak + 1 else 1
        return s.copy(streak = next, lastDay = today)
    }

    private fun dayString(offsetDays: Int = 0): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, offsetDays)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.time)
    }
}