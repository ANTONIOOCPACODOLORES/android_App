package com.example.englishapp.data

import android.content.Context
import java.security.MessageDigest
import java.util.UUID

/** Cuentas guardadas en el teléfono. La contraseña se guarda como hash SHA-256 con sal. */
class LocalBackend(context: Context) : Backend {
    private val prefs = context.getSharedPreferences("auth_local", Context.MODE_PRIVATE)

    private fun clean(email: String) = email.trim().lowercase()

    private fun hash(pass: String, salt: String): String =
        MessageDigest.getInstance("SHA-256").digest((salt + pass).toByteArray())
            .joinToString("") { "%02x".format(it) }

    override suspend fun register(name: String, email: String, pass: String): String? {
        val e = clean(email)
        if (prefs.contains("hash_$e")) return "Ese correo ya está registrado."
        val salt = UUID.randomUUID().toString()
        prefs.edit()
            .putString("name_$e", name.trim())
            .putString("salt_$e", salt)
            .putString("hash_$e", hash(pass, salt))
            .putString("session", e)
            .apply()
        return null
    }

    override suspend fun login(email: String, pass: String): String? {
        val e = clean(email)
        val salt = prefs.getString("salt_$e", null)
        val saved = prefs.getString("hash_$e", null)
        if (salt == null || saved == null) return "No existe una cuenta con ese correo."
        if (hash(pass, salt) != saved) return "Contraseña incorrecta."
        prefs.edit().putString("session", e).apply()
        return null
    }

    override fun logout() {
        prefs.edit().remove("session").apply()
    }

    override fun currentUser(): UserProfile? {
        val e = prefs.getString("session", null) ?: return null
        val name = prefs.getString("name_$e", null)?.takeIf { it.isNotBlank() } ?: e.substringBefore("@")
        return UserProfile(e, name, e)
    }

    override suspend fun loadProgress(): Map<Int, LessonProgress> {
        val e = prefs.getString("session", null) ?: return emptyMap()
        return LessonCatalog.mapNotNull { l ->
            prefs.getString("prog_${e}_${l.id}", null)?.split(",")?.takeIf { it.size == 3 }?.let { p ->
                l.id to LessonProgress(p[0].toInt(), p[1].toInt(), p[2].toInt())
            }
        }.toMap()
    }

    override suspend fun saveProgress(lessonId: Int, progress: LessonProgress) {
        val e = prefs.getString("session", null) ?: return
        prefs.edit()
            .putString("prog_${e}_$lessonId", "${progress.best},${progress.last},${progress.attempts}")
            .apply()
    }
}
