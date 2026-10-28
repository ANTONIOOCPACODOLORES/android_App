package com.example.englishapp.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Firebase Authentication (usuarios) + Cloud Firestore (progreso):
 *   users/{uid}                    -> { name, email }
 *   users/{uid}/progress/{lessonId} -> { best, last, attempts }   (ids 1..5 lecciones, 101..103 vocabulario)
 *   users/{uid}/meta/stats          -> palabras, tiempo, racha, meta de hoy y banner
 */
class FirebaseBackend : Backend {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun message(e: Exception): String = when (e) {
        is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado."
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)."
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo."
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
        is FirebaseNetworkException -> "Sin conexión a internet."
        else -> e.localizedMessage ?: "Ocurrió un error. Intenta de nuevo."
    }

    override suspend fun register(name: String, email: String, pass: String): String? = try {
        val result = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
        val user = result.user!!
        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name.trim()).build()).await()
        db.collection("users").document(user.uid)
            .set(mapOf("name" to name.trim(), "email" to email.trim())).await()
        null
    } catch (e: Exception) {
        message(e)
    }

    override suspend fun login(email: String, pass: String): String? = try {
        auth.signInWithEmailAndPassword(email.trim(), pass).await()
        null
    } catch (e: Exception) {
        message(e)
    }

    override fun logout() = auth.signOut()

    override fun currentUser(): UserProfile? = auth.currentUser?.let {
        val mail = it.email ?: ""
        UserProfile(it.uid, it.displayName?.takeIf { n -> n.isNotBlank() } ?: mail.substringBefore("@"), mail)
    }

    override suspend fun loadProgress(): Map<Int, LessonProgress> {
        val uid = auth.currentUser?.uid ?: return emptyMap()
        val snap = db.collection("users").document(uid).collection("progress").get().await()
        return snap.documents.mapNotNull { d ->
            d.id.toIntOrNull()?.let { id ->
                id to LessonProgress(
                    d.getLong("best")?.toInt() ?: 0,
                    d.getLong("last")?.toInt() ?: 0,
                    d.getLong("attempts")?.toInt() ?: 0,
                )
            }
        }.toMap()
    }

    override suspend fun saveProgress(lessonId: Int, progress: LessonProgress) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("progress").document(lessonId.toString())
            .set(mapOf("best" to progress.best, "last" to progress.last, "attempts" to progress.attempts))
            .await()
    }
    private fun statsDoc(uid: String) =
        db.collection("users").document(uid).collection("meta").document("stats")

    override suspend fun loadStats(): UserStats {
        val uid = auth.currentUser?.uid ?: return UserStats()
        val d = statsDoc(uid).get().await()
        if (!d.exists()) return UserStats()
        val words = (d.get("words") as? List<*>)?.filterIsInstance<String>()?.toSet() ?: emptySet()
        return UserStats(
            words = words,
            studySeconds = d.getLong("studySeconds") ?: 0L,
            streak = d.getLong("streak")?.toInt() ?: 0,
            lastDay = d.getString("lastDay") ?: "",
            todayDate = d.getString("todayDate") ?: "",
            todayLessons = d.getLong("todayLessons")?.toInt() ?: 0,
            todayPractices = d.getLong("todayPractices")?.toInt() ?: 0,
            todaySeconds = d.getLong("todaySeconds") ?: 0L,
            banner = d.getLong("banner")?.toInt() ?: 0,
        )
    }

    override suspend fun saveStats(stats: UserStats) {
        val uid = auth.currentUser?.uid ?: return
        statsDoc(uid).set(
            mapOf(
                "words" to stats.words.toList(),
                "studySeconds" to stats.studySeconds,
                "streak" to stats.streak,
                "lastDay" to stats.lastDay,
                "todayDate" to stats.todayDate,
                "todayLessons" to stats.todayLessons,
                "todayPractices" to stats.todayPractices,
                "todaySeconds" to stats.todaySeconds,
                "banner" to stats.banner,
            )
        ).await()
    }
}