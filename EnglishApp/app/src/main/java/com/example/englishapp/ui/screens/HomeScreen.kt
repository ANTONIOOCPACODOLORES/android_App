package com.example.englishapp.ui.screens

import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import com.example.englishapp.data.Session

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab

@Composable
fun HomeScreen(
    onLessons: () -> Unit,
    onPractice: () -> Unit,
    onProgress: () -> Unit,
    onTab: (Tab) -> Unit,
) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF8FAFD))) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Greeting()
            HeroCard()

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.MenuBook,
                    iconBg = Color(0xFFF5F3FF),
                    iconTint = Color(0xFF7C3AED),
                    title = "Lecciones",
                    subtitle = "Gramática y vocabulario",
                    onClick = onLessons,
                )
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.VerifiedUser,
                    iconBg = Color(0xFFECFDF5),
                    iconTint = Color(0xFF10B981),
                    title = "Práctica",
                    subtitle = "Ejercicios y quizzes",
                    onClick = onPractice,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.BarChart,
                    iconBg = Color(0xFFFAF5FF),
                    iconTint = Color(0xFF9333EA),
                    title = "Mi progreso",
                    subtitle = "Ver estadísticas",
                    onClick = onProgress,
                )
                Spacer(Modifier.weight(1f))
            }

            DailyTaskCard(onLessons = onLessons, onPractice = onPractice)
        }
        AppBottomBar(selected = Tab.Inicio, activeColor = Color(0xFF2563EB), onSelect = onTab)
    }
}

@Composable
private fun Greeting() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "¡Hola, ${Session.user?.name ?: "estudiante"}!",
                color = Color(0xFF0B2347),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = (-0.6).sp,
                fontWeight = FontWeight.Bold,
            )
            Text("Sigue practicando y alcanza tu meta", color = Color(0xFF64748B), fontSize = 12.sp, lineHeight = 16.sp)
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(1.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFDBEAFE))
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Perfil",
                tint = Color(0xFF334155),
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

@Composable
private fun HeroCard() {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .shadow(3.dp, shape, ambientColor = Color(0x333B82F6), spotColor = Color(0x333B82F6))
            .clip(shape)
            .background(Color(0xFF1A73E8)),
    ) {
        // Forma decorativa de fondo
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 24.dp, y = 40.dp)
                .size(192.dp)
                .background(Color(0x3360A5FA), CircleShape)
        )
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                Text(
                    "Nivel A2",
                    color = Color.White,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.45.sp,
                    fontWeight = FontWeight.Bold,
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Tu progreso", color = Color(0xFFDBEAFE), fontSize = 11.sp, lineHeight = 16.5.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .width(112.dp)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Color(0x6660A5FA))
                        ) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(Session.overall / 100f)
                                    .background(Color.White, CircleShape)
                            )
                        }
                        Text(
                            "${Session.overall}%",
                            color = Color.White,
                            fontSize = 11.sp,
                            lineHeight = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
            StudentBadge()
        }
    }
}

/** Ilustración del estudiante + mini bandera UK, dibujadas con formas. */
@Composable
private fun StudentBadge() {
    Box(Modifier.size(width = 112.dp, height = 96.dp)) {
        // Mini bandera UK
        Canvas(
            Modifier
                .offset(x = 8.dp, y = 4.dp)
                .size(width = 24.dp, height = 16.dp)
                .clip(RoundedCornerShape(2.dp))
        ) {
            drawRect(Color(0xFF1E3A8A))
            val w = size.width
            val h = size.height
            val white = Stroke(width = h * 0.22f)
            drawLine(Color.White, Offset(0f, 0f), Offset(w, h), strokeWidth = white.width)
            drawLine(Color.White, Offset(w, 0f), Offset(0f, h), strokeWidth = white.width)
            drawLine(Color(0xFFDC2626), Offset(0f, 0f), Offset(w, h), strokeWidth = h * 0.09f)
            drawLine(Color(0xFFDC2626), Offset(w, 0f), Offset(0f, h), strokeWidth = h * 0.09f)
            drawRect(Color.White, Offset(w * 0.4f, 0f), Size(w * 0.2f, h))
            drawRect(Color.White, Offset(0f, h * 0.35f), Size(w, h * 0.3f))
            drawRect(Color(0xFFDC2626), Offset(w * 0.45f, 0f), Size(w * 0.1f, h))
            drawRect(Color(0xFFDC2626), Offset(0f, h * 0.42f), Size(w, h * 0.16f))
        }
        // Estudiante
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .size(width = 48.dp, height = 68.dp)
        ) {
            // Torso / chamarra
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = 48.dp, height = 44.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color(0xFF1E3A8A)),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Box(
                    Modifier
                        .padding(bottom = 3.dp)
                        .rotate(6f)
                        .size(width = 24.dp, height = 20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFFBBF24))
                        .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(2.dp))
                )
            }
            // Cabeza
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .size(width = 30.dp, height = 29.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFBCBB0))
                    .border(1.dp, Color(0x1A0F172A), CircleShape),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    Modifier
                        .size(width = 28.dp, height = 16.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(Color(0xFF0F172A))
                )
            }
        }
    }
}

@Composable
private fun FeatureCard(
    modifier: Modifier,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .height(128.dp)
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), shape)
            .clickable(onClick = onClick)
            .padding(17.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
        }
        Column {
            Text(title, color = Color(0xFF1E293B), fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color(0xFF94A3B8), fontSize = 11.sp, lineHeight = 16.5.sp)
        }
    }
}

@Composable
private fun DailyTaskCard(onLessons: () -> Unit, onPractice: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val lessonDone = Session.todayLessons >= 1
    val practiceDone = Session.todayPractices >= 1
    val done = (if (lessonDone) 1 else 0) + (if (practiceDone) 1 else 0)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), shape)
            .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Hoy", color = Color(0xFF1E293B), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
            Text("$done/2", color = Color(0xFF64748B), fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold)
        }
        Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(Color(0xFFE2E8F0))) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(done / 2f)
                    .background(Color(0xFF10B981), CircleShape)
            )
        }
        DailyTaskRow("Completa 1 lección", lessonDone, onLessons)
        DailyTaskRow("Completa 1 práctica", practiceDone, onPractice)
        if (done == 2) {
            Text("¡Meta del día cumplida!", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DailyTaskRow(label: String, done: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = if (done) Color(0xFF10B981) else Color(0xFFCBD5E1),
            modifier = Modifier.size(20.dp),
        )
        Text(
            label,
            color = if (done) Color(0xFF94A3B8) else Color(0xFF475569),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f).padding(start = 10.dp),
        )
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
    }
}