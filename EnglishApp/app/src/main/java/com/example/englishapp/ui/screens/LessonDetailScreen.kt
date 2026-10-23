package com.example.englishapp.ui.screens

import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.LessonData

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF1D6BE5)
private val Strong = Color(0xFF0F172A)
private val Body = Color(0xFF334155)

@Composable
fun LessonDetailScreen(lesson: LessonData, onBack: () -> Unit, onNext: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            // Barra superior
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Volver atrás",
                    tint = Color(0xFF0F172A),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onBack)
                        .padding(8.dp),
                )
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(Color(0xB3E0F2FE))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("${lesson.id}/${LessonCatalog.size}", color = Blue, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(19.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        lesson.title,
                        color = Color(0xFF102A43),
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.6).sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(lesson.description, color = Color(0xFF94A3B8), fontSize = 14.sp, lineHeight = 20.sp)
                }

                // Pastilla "Explicación"
                val pill = RoundedCornerShape(16.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(pill)
                        .background(Color(0xFFF0F7FF))
                        .border(1.dp, Color(0xFFE0EFFF), pill)
                        .padding(start = 17.dp, end = 17.dp, top = 14.dp, bottom = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.MenuBook, null, tint = Blue, modifier = Modifier.size(24.dp))
                    Text(
                        "Explicación",
                        color = Color(0xFF102A43),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = (-0.35).sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }

                // Usos
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 1.dp)) {
                    Text(
                        "Lo usamos para:",
                        color = Color(0xFF1E293B),
                        fontSize = 13.5.sp,
                        lineHeight = 21.94.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Column(Modifier.padding(start = 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        lesson.uses.forEach { BulletItem(AnnotatedString(it), Color(0xFF475569), 21.94.sp) }
                    }
                }

                // Estructura básica
                val box = RoundedCornerShape(16.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(box)
                        .background(Color(0x80F0F9FF))
                        .border(1.dp, Color(0xFFE0F2FE), box)
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Text("Estructura básica:", color = Blue, fontSize = 13.5.sp, lineHeight = 20.25.sp, fontWeight = FontWeight.Bold)
                    Text(lesson.structureTitle, color = Strong, fontSize = 13.5.sp, lineHeight = 20.25.sp, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        lesson.structureLines.map { markup(it) }.forEach {
                            Text(it, color = Body, fontSize = 13.5.sp, lineHeight = 20.25.sp)
                        }
                    }
                }

                // Ejemplos
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ejemplos:", color = Color(0xFF1E293B), fontSize = 13.5.sp, lineHeight = 20.25.sp, fontWeight = FontWeight.Bold)
                    Column(Modifier.padding(start = 4.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        lesson.examples.forEach { BulletItem(markup(it), Body, 20.25.sp) }
                    }
                }
            }
        }

        // Botón flotante "Siguiente"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to Color(0x00FFFFFF),
                        0.5f to Color.White,
                        1f to Color.White,
                    )
                )
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Blue)
                    .clickable(onClick = onNext),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Hacer formulario", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = Color.White,
                    modifier = Modifier.padding(start = 8.dp).size(16.dp),
                )
            }
        }
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.bold(text: String) {
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Strong)) { append(text) }
}

@Composable
private fun BulletItem(text: AnnotatedString, color: Color, lineHeight: androidx.compose.ui.unit.TextUnit) {
    Row {
        Text(
            "•",
            color = Color(0xFF64748B),
            fontSize = 13.5.sp,
            lineHeight = lineHeight,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(text, color = color, fontSize = 13.5.sp, lineHeight = lineHeight)
    }
}

/** Convierte "texto **negrita** texto" en AnnotatedString. */
private fun markup(s: String): AnnotatedString = buildAnnotatedString {
    s.split("**").forEachIndexed { i, part -> if (i % 2 == 1) bold(part) else append(part) }
}
