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
 *   users/{uid}/progress/{lessonId} -> { best, last, attempts }
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
}
