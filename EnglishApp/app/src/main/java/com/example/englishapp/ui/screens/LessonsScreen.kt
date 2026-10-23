package com.example.englishapp.ui.screens

import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.LessonData
import com.example.englishapp.data.Session

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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

@Composable
fun LessonsScreen(
    onBack: () -> Unit,
    onLesson: (Int) -> Unit,
    onVocabulary: () -> Unit,
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
                    "Elige una lección y empieza a aprender",
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
                            .clickable {
                                category = i
                                if (i == 1) onVocabulary()
                            }
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

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LessonCatalog.forEach { l -> LessonCard(l, Session.progress[l.id]?.best ?: 0, onClick = { onLesson(l.id) }) }
            }
        }
    }
}

@Composable
private fun LessonCard(lesson: LessonData, percent: Int, onClick: () -> Unit) {
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
            Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(lesson.color),
            contentAlignment = Alignment.Center,
        ) {
            Icon(lesson.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Column(
            modifier = Modifier.weight(1f).padding(start = 14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text("${lesson.id}. ${lesson.title}", color = Color(0xFF0F172A), fontSize = 14.sp, lineHeight = 19.25.sp, fontWeight = FontWeight.Bold)
            Text(lesson.description, color = Color(0xFF94A3B8), fontSize = 11.sp, lineHeight = 13.75.sp)
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
        Icon(Icons.Filled.ChevronRight, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(20.dp))
    }
}
