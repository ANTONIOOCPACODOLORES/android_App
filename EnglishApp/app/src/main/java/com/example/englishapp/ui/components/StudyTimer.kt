package com.example.englishapp.ui.components

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.example.englishapp.data.Session

/**
 * Pon TrackStudyTime() al inicio de una pantalla de estudio: al salir de ella,
 * el tiempo que estuvo abierta se suma al "Tiempo de estudio" del usuario.
 */
@Composable
fun TrackStudyTime() {
    DisposableEffect(Unit) {
        val start = SystemClock.elapsedRealtime()
        onDispose {
            Session.addStudySeconds((SystemClock.elapsedRealtime() - start) / 1000)
        }
    }
}