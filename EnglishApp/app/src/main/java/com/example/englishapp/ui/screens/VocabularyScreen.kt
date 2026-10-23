package com.example.englishapp.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab
import java.util.Locale

private data class Word(
    val en: String,
    val es: String,
    val icon: ImageVector,
    val bg: Color,
    val tint: Color,
)

private val Words = listOf(
    Word("airport", "aeropuerto", Icons.Filled.Flight, Color(0xFFE0F2FE), Color(0xFF0284C7)),
    Word("hotel", "hotel", Icons.Filled.Hotel, Color(0xFFFEF3C7), Color(0xFFD97706)),
    Word("restaurant", "restaurante", Icons.Filled.Restaurant, Color(0xFFFFEDD5), Color(0xFFEA580C)),
    Word("friend", "amigo/a", Icons.Filled.Groups, Color(0xFFD1FAE5), Color(0xFF059669)),
    Word("house", "casa", Icons.Filled.Home, Color(0xFFE0E7FF), Color(0xFF4F46E5)),
    Word("city", "ciudad", Icons.Filled.LocationCity, Color(0xFFF3E8FF), Color(0xFF9333EA)),
)

private val Blue = Color(0xFF1976D2)

@Composable
fun VocabularyScreen(onBack: () -> Unit, onTab: (Tab) -> Unit) {
    var query by remember { mutableStateOf("") }
    var category by remember { mutableIntStateOf(0) }
    val categories = listOf("Todos", "Viajes", "Comida", "Trabajo")

    // Pronunciación con el motor de texto a voz del dispositivo
    val context = LocalContext.current
    val tts = remember {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) engine?.language = Locale.US
        }
        engine
    }
    DisposableEffect(Unit) { onDispose { tts?.shutdown() } }

    val visible = Words.filter {
        query.isBlank() ||
            it.en.contains(query.trim(), ignoreCase = true) ||
            it.es.contains(query.trim(), ignoreCase = true)
    }

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Volver atrás",
                tint = Color(0xFF0F172A),
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack)
                    .padding(4.dp)
                    .size(24.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        "Vocabulario",
                        color = Color(0xFF102A54),
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.6).sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        "Aprende palabras útiles para tu día a día",
                        color = Color(0xFF64748B),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                // Búsqueda
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xE6F1F5F9)),
                ) {
                    Icon(
                        Icons.Filled.Search,
                        null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 14.dp).size(16.dp),
                    )
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF0F172A)),
                        cursorBrush = SolidColor(Blue),
                        modifier = Modifier.fillMaxSize().padding(start = 41.dp, end = 17.dp),
                        decorationBox = { inner ->
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                                if (query.isEmpty()) {
                                    Text("Buscar palabra...", color = Color(0xFF94A3B8), fontSize = 14.sp)
                                }
                                inner()
                            }
                        },
                    )
                }

                // Filtros
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    categories.forEachIndexed { i, name ->
                        val active = i == category
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (active) Blue else Color(0xFFF1F5F9))
                                .clickable { category = i }
                                .padding(horizontal = if (active) 20.dp else 16.dp, vertical = 6.dp),
                        ) {
                            Text(
                                name,
                                color = if (active) Color.White else Color(0xFF475569),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }

                // Tarjetas de palabras
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    visible.forEach { word ->
                        WordCard(word, onSpeak = {
                            tts?.speak(word.en, TextToSpeech.QUEUE_FLUSH, null, word.en)
                        })
                    }
                }
            }
        }
        AppBottomBar(selected = Tab.Lecciones, activeColor = Blue, onSelect = onTab)
    }
}

@Composable
private fun WordCard(word: Word, onSpeak: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape, ambientColor = Color(0x0A000000), spotColor = Color(0x0A000000))
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), shape)
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(word.bg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(word.icon, null, tint = word.tint, modifier = Modifier.size(24.dp))
            }
            Column(Modifier.padding(start = 14.dp)) {
                Text(word.en, color = Color(0xFF0F172A), fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
                Text(word.es, color = Color(0xFF64748B), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
        Icon(
            Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Pronunciar ${word.en}",
            tint = Color(0xFF94A3B8),
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onSpeak)
                .padding(8.dp)
                .size(20.dp),
        )
    }
}
