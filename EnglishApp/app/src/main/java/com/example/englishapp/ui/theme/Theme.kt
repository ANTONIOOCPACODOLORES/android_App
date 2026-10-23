package com.example.englishapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(
    primary = Color(0xFF1967D2),
    onPrimary = Color.White,
    background = Color.White,
    surface = Color.White,
)

@Composable
fun EnglishAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content)
}
