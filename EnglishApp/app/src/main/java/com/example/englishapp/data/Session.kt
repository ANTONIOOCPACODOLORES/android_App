package com.example.englishapp.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp

/** Usuario actual + progreso por lección. Elige Firebase si hay google-services.json, si no, local. */
object Session {
    const val PASS_PERCENT = 80

    private lateinit var backend: Backend

    var isCloud by mutableStateOf(false)
        private set
    var user by mutableStateOf<UserProfile?>(null)
        private set
    val progress = mutableStateMapOf<Int, LessonProgress>()

    /** % general = promedio del mejor resultado de cada lección. */
    val overall: Int get() = LessonCatalog.sumOf { progress[it.id]?.best ?: 0 } / LessonCatalog.size
    val completed: Int get() = LessonCatalog.count { (progress[it.id]?.best ?: 0) >= PASS_PERCENT }

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
    }

    suspend fun register(name: String, email: String, pass: String): String? {
        val err = backend.register(name, email, pass)
        if (err == null) { user = backend.currentUser(); loadProgress() }
        return err
    }

    suspend fun login(email: String, pass: String): String? {
        val err = backend.login(email, pass)
        if (err == null) { user = backend.currentUser(); loadProgress() }
        return err
    }

    fun logout() {
        backend.logout()
        user = null
        progress.clear()
    }

    suspend fun saveLessonResult(lessonId: Int, correct: Int, total: Int) {
        if (total == 0) return
        val pct = correct * 100 / total
        val old = progress[lessonId] ?: LessonProgress()
        val updated = LessonProgress(maxOf(old.best, pct), pct, old.attempts + 1)
        progress[lessonId] = updated
        try { backend.saveProgress(lessonId, updated) } catch (_: Exception) { }
    }
}
