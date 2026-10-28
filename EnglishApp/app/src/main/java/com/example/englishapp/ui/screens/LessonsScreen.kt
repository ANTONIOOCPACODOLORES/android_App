package com.example.englishapp.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.Session
import com.example.englishapp.data.VocabTopics
import com.example.englishapp.data.VocabWords

/**
 * @param onVocabulary abre el vocabulario; recibe el id del tema (101, 102, 103) o 0 para ver todo.
 */
@Composable
fun LessonsScreen(
    onBack: () -> Unit,
    onLesson: (Int) -> Unit,
    onVocabulary: (Int) -> Unit,
) {
    var category by remember { mutableIntStateOf(0) }
    val categories = listOf("Gramática", "Vocabulario", "Expresiones")

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Volver",
            tint = Color(0xFF0F1E36),
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack)
                .padding(4.dp)
                .size(20.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Lecciones",
                    color = Color(0xFF0F1E36),
                    fontSize = 26.sp,
                    lineHeight = 32.5.sp,
                    letterSpacing = (-0.65).sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    when (category) {
                        0 -> "Elige una lección y empieza a aprender"
                        1 -> "Elige un tema y aprende palabras nuevas"
                        else -> "Frases y expresiones útiles"
                    },
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                )
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                categories.forEachIndexed { i, name ->
                    val active = i == category
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (active) Color(0xFF136DEC) else Color(0xFFF1F5F9))
                            .clickable { category = i }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            name,
                            color = if (active) Color.White else Color(0xFF475569),
                            fontSize = 11.sp,
                            lineHeight = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            when (category) {
                0 -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LessonCatalog.forEach { l ->
                        ItemCard(
                            title = "${l.id}. ${l.title}",
                            subtitle = l.description,
                            icon = l.icon,
                            color = l.color,
                            percent = Session.progress[l.id]?.best ?: 0,
                            onClick = { onLesson(l.id) },
                        )
                    }
                }

                1 -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    VocabTopics.forEach { t ->
                        val total = VocabWords.count { it.topicId == t.id }
                        val learned = VocabWords.count { it.topicId == t.id && it.en in Session.stats.words }
                        ItemCard(
                            title = t.name,
                            subtitle = "$learned de $total palabras aprendidas",
                            icon = t.icon,
                            color = t.color,
                            percent = Session.progress[t.id]?.best ?: 0,
                            onClick = { onVocabulary(t.id) },
                        )
                    }
                    ItemCard(
                        title = "Todo el vocabulario",
                        subtitle = "Busca entre todas las palabras",
                        icon = Icons.Filled.Search,
                        color = Color(0xFF64748B),
                        percent = null,
                        onClick = { onVocabulary(0) },
                    )
                }

                else -> Box(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(20.dp),
                ) {
                    Text(
                        "Las expresiones llegarán pronto.",
                        color = Color(0xFF64748B), fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

/** Tarjeta genérica de la lista (lección de gramática o tema de vocabulario). percent = null oculta el %. */
@Composable
private fun ItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    percent: Int?,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                3.dp, shape,
                ambientColor = Color(0x1A0F1E36),
                spotColor = Color(0x1A0F1E36),
            )
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), shape)
            .clickable(onClick = onClick)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(color),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(title, color = Color(0xFF0F172A), fontSize = 14.sp, lineHeight = 19.25.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp, lineHeight = 13.75.sp)
            if (percent != null) {
                Box(
                    Modifier
                        .padding(top = 9.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("$percent%", color = Color(0xFF64748B), fontSize = 10.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Icon(Icons.Filled.ChevronRight, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(20.dp))
    }
}