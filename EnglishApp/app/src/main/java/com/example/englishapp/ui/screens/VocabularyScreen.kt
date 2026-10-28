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
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.data.Session
import com.example.englishapp.data.VocabTopic
import com.example.englishapp.data.VocabTopics
import com.example.englishapp.data.VocabWord
import com.example.englishapp.data.VocabWords
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab
import com.example.englishapp.ui.components.TrackStudyTime
import java.util.Locale

private val Blue = Color(0xFF1976D2)

/**
 * @param initialTopic 0 = todos los temas; si no, el id del tema (101 Viajes, 102 Comida, 103 Trabajo).
 * @param onQuiz abre el mini formulario del tema (usa el id del tema).
 */
@Composable
fun VocabularyScreen(
    initialTopic: Int,
    onBack: () -> Unit,
    onTab: (Tab) -> Unit,
    onQuiz: (Int) -> Unit,
) {
    TrackStudyTime()

    var query by remember { mutableStateOf("") }
    var selectedTopic by remember { mutableIntStateOf(initialTopic) }
    val filters: List<Pair<Int, String>> = listOf(0 to "Todos") + VocabTopics.map { it.id to it.name }

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

    val visible = VocabWords.filter {
        (selectedTopic == 0 || it.topicId == selectedTopic) &&
            (query.isBlank() ||
                it.en.contains(query.trim(), ignoreCase = true) ||
                it.es.contains(query.trim(), ignoreCase = true))
    }
    val topic: VocabTopic? = VocabTopics.firstOrNull { it.id == selectedTopic }
    val inScope = VocabWords.filter { selectedTopic == 0 || it.topicId == selectedTopic }
    val learnedInScope = inScope.count { it.en in Session.stats.words }

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
                        "Aprendidas: $learnedInScope de ${inScope.size}",
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

                // Filtros por tema
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    filters.forEach { (id, name) ->
                        val active = id == selectedTopic
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (active) Blue else Color(0xFFF1F5F9))
                                .clickable { selectedTopic = id }
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

                // Mini formulario del tema
                if (topic != null) {
                    val best = Session.progress[topic.id]?.best ?: 0
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(topic.bg)
                            .clickable { onQuiz(topic.id) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(topic.icon, null, tint = topic.color, modifier = Modifier.size(24.dp))
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(
                                "Practicar ${topic.name}",
                                color = topic.color, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "Mini formulario · mejor resultado: $best%",
                                color = Color(0xFF475569), fontSize = 11.sp,
                            )
                        }
                    }
                }

                // Tarjetas de palabras
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    visible.forEach { word ->
                        val wordTopic = VocabTopics.first { it.id == word.topicId }
                        val learned = word.en in Session.stats.words
                        WordCard(
                            word = word,
                            topic = wordTopic,
                            learned = learned,
                            onSpeak = { tts?.speak(word.en, TextToSpeech.QUEUE_FLUSH, null, word.en) },
                            onToggleLearned = { Session.setWordLearned(word.en, !learned) },
                        )
                    }
                    if (visible.isEmpty()) {
                        Text("No se encontraron palabras.", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    }
                }
            }
        }
        AppBottomBar(selected = Tab.Lecciones, activeColor = Blue, onSelect = onTab)
    }
}

@Composable
private fun WordCard(
    word: VocabWord,
    topic: VocabTopic,
    learned: Boolean,
    onSpeak: () -> Unit,
    onToggleLearned: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape, ambientColor = Color(0x0A000000), spotColor = Color(0x0A000000))
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), shape)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(topic.bg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(topic.icon, null, tint = topic.color, modifier = Modifier.size(24.dp))
        }
        Column(Modifier.weight(1f).padding(start = 14.dp, end = 8.dp)) {
            Text(word.en, color = Color(0xFF0F172A), fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
            Text(word.es, color = Color(0xFF64748B), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
            Text(
                word.example,
                color = Color(0xFF94A3B8), fontSize = 11.sp, lineHeight = 15.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 3.dp),
            )
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
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = if (learned) "Quitar de aprendidas" else "Marcar como aprendida",
            tint = if (learned) Color(0xFF10B981) else Color(0xFFCBD5E1),
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onToggleLearned)
                .padding(8.dp)
                .size(20.dp),
        )
    }
}