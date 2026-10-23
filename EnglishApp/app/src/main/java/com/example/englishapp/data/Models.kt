package com.example.englishapp.data

data class UserProfile(val id: String, val name: String, val email: String)

/** best = mejor %, last = % del último intento, attempts = número de intentos. */
data class LessonProgress(val best: Int = 0, val last: Int = 0, val attempts: Int = 0)

/**
 * Contrato que cumplen los dos backends:
 *  - FirebaseBackend (nube)  -> se usa si existe app/google-services.json
 *  - LocalBackend (teléfono) -> se usa si no existe, para poder probar sin internet
 * Los métodos que fallan devuelven un mensaje de error (o null si salió bien).
 */
interface Backend {
    suspend fun register(name: String, email: String, pass: String): String?
    suspend fun login(email: String, pass: String): String?
    fun logout()
    fun currentUser(): UserProfile?
    suspend fun loadProgress(): Map<Int, LessonProgress>
    suspend fun saveProgress(lessonId: Int, progress: LessonProgress)
}
