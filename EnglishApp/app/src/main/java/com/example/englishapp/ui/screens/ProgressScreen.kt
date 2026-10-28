package com.example.englishapp.ui.screens

import com.example.englishapp.data.VocabTopics
import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.Session
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab

private val Blue = Color(0xFF1D6FE9)
private val Ink = Color(0xFF0C1C38)

@Composable
fun ProgressScreen(onTab: (Tab) -> Unit) {
    var tab by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Mi progreso",
                color = Ink,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.6).sp,
                fontWeight = FontWeight.Bold,
            )

            // Selector General / Por lección
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(Color(0xFFF1F4F9))
                    .padding(4.dp),
            ) {
                listOf("General", "Por lección").forEachIndexed { i, label ->
                    val selected = tab == i
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(CircleShape)
                            .background(if (selected) Blue else Color.Transparent)
                            .clickable { tab = i },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            label,
                            color = if (selected) Color.White else Color(0xFF64748B),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            if (tab == 1) {
                LessonBreakdown()
            } else {
            // Medidor circular
            Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.size(176.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxSize()) {
                        val stroke = 12.dp.toPx()
                        val inset = stroke / 2 + 7.dp.toPx()
                        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                        drawArc(
                            color = Color(0xFFDBEAFE),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = Stroke(width = stroke),
                        )
                        drawArc(
                            color = Blue,
                            startAngle = -90f,
                            sweepAngle = 360f * Session.overall / 100f,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round),
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${Session.overall}%",
                            color = Ink,
                            fontSize = 30.sp,
                            lineHeight = 37.5.sp,
                            letterSpacing = (-0.75).sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "Nivel A2",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Métricas 2x2
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    Modifier.weight(1f), Icons.Filled.Checklist,
                    Color(0xFFECFDF5), Color(0xFF10B981), "Lecciones\ncompletadas", "${Session.completed} / ${Session.totalItems}",
                )
                MetricCard(
                    Modifier.weight(1f), Icons.Filled.AutoAwesome,
                    Color(0xFFEFF6FF), Color(0xFF3B82F6), "Palabras aprendidas", Session.stats.words.size.toString(),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    Modifier.weight(1f), Icons.Filled.Schedule,
                    Color(0xFFEEF2FF), Color(0xFF6366F1), "Tiempo de estudio", formatStudyTime(Session.stats.studySeconds),
                )
                MetricCard(
                    Modifier.weight(1f), Icons.Filled.LocalFireDepartment,
                    Color(0xFFFFF1F2), Color(0xFFF43F5E), "Racha actual", if (Session.streakShown == 1) "1 día" else "${Session.streakShown} días",
                )
            }

            // Nivel actual
            val shape = RoundedCornerShape(16.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, shape)
                    .clip(shape)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFEAF0F6), shape)
                    .padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Tu nivel actual", color = Color(0xFF94A3B8), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
                    Text("A2 - Pre-Intermediate", color = Blue, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    "Puedes mantener conversaciones sobre temas cotidianos, describir experiencias y dar tu opinión.",
                    color = Color(0xFF475569),
                    fontSize = 12.sp,
                    lineHeight = 19.5.sp,
                )
            }
            }
        }
        AppBottomBar(selected = Tab.Practica, activeColor = Blue, onSelect = onTab)
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    value: String,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .height(96.dp)
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFEAF0F6), shape)
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
            Text(
                label,
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                lineHeight = 13.75.sp,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        Text(
            value,
            color = Ink,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.4).sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

/** Pestaña "Por lección": % de cada lección del usuario. */
@Composable
private fun LessonBreakdown() {
    val shape = RoundedCornerShape(16.dp)
    breakdownRows().forEach { (id, title) ->
        val p = Session.progress[id]
        val best = p?.best ?: 0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .border(1.dp, Color(0xFFEAF0F6), shape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("$best%", color = Blue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Box(Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Color(0xFFDBEAFE))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(best / 100f).background(Blue, CircleShape))
            }
            Text(
                if (p == null) "Aún no has hecho el formulario"
                else "Intentos: ${p.attempts}  ·  Último: ${p.last}%",
                color = Color(0xFF94A3B8), fontSize = 11.sp,
            )
        }
    }
}

/** Filas de la pestaña "Por lección": las lecciones de gramática y los temas de vocabulario. */
private fun breakdownRows(): List<Pair<Int, String>> =
    LessonCatalog.map { it.id to "${it.id}. ${it.title}" } +
        VocabTopics.map { it.id to "Vocabulario: ${it.name}" }

/** 0 -> "0 min", 2700 -> "45 min", 16080 -> "4 h 28 min". */
private fun formatStudyTime(seconds: Long): String {
    val totalMin = seconds / 60
    val h = totalMin / 60
    val m = totalMin % 60
    return if (h > 0) "$h h $m min" else "$m min"
}