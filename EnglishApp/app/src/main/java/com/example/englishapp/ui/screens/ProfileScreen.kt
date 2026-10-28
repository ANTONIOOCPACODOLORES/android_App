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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.data.Session
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab

/** Banners disponibles para el fondo del perfil. El usuario guarda el índice de su elección. */
private class Banner(val name: String, val colors: List<Color>)

private val Banners = listOf(
    Banner("Océano", listOf(Color(0xFF1D6FE9), Color(0xFF06B6D4))),
    Banner("Atardecer", listOf(Color(0xFFF97316), Color(0xFFEC4899))),
    Banner("Bosque", listOf(Color(0xFF047857), Color(0xFF65A30D))),
    Banner("Lavanda", listOf(Color(0xFF6D28D9), Color(0xFFC084FC))),
    Banner("Cereza", listOf(Color(0xFFBE123C), Color(0xFFFB7185))),
    Banner("Noche", listOf(Color(0xFF0F172A), Color(0xFF4338CA))),
    Banner("Menta", listOf(Color(0xFF0F766E), Color(0xFF2DD4BF))),
    Banner("Dorado", listOf(Color(0xFFB45309), Color(0xFFF59E0B))),
)

@Composable
fun ProfileScreen(onTab: (Tab) -> Unit, onLogout: () -> Unit) {
    val user = Session.user
    val shape = RoundedCornerShape(16.dp)
    val selected = Session.stats.banner.coerceIn(0, Banners.lastIndex)
    val banner = Banners[selected]

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
            Text("Perfil", color = Color(0xFF0C1C38), fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // Banner con el avatar y los datos del usuario encima
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(shape)
                    .background(Brush.linearGradient(banner.colors)),
            ) {
                Row(
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            (user?.name?.firstOrNull() ?: 'E').uppercase(),
                            color = banner.colors.first(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Column(Modifier.padding(start = 14.dp)) {
                        Text(user?.name ?: "Estudiante", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(user?.email ?: "", color = Color(0xE6FFFFFF), fontSize = 13.sp)
                    }
                }
            }

            Text(
                if (Session.isCloud) "☁  Cuenta en la nube (Firebase)" else "📱  Cuenta local en este dispositivo",
                color = Color(0xFF475569), fontSize = 12.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(Modifier.weight(1f), "Progreso general", "${Session.overall}%")
                StatTile(Modifier.weight(1f), "Aprobadas", "${Session.completed} / ${Session.totalItems}")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(Modifier.weight(1f), "Palabras aprendidas", "${Session.stats.words.size}")
                StatTile(
                    Modifier.weight(1f), "Racha actual",
                    if (Session.streakShown == 1) "1 día" else "${Session.streakShown} días",
                )
            }

            // Selector de banner
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Elige tu banner", color = Color(0xFF0C1C38), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Banners.forEachIndexed { i, b ->
                        val isSelected = i == selected
                        Column(
                            modifier = Modifier.clickable { Session.setBanner(i) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Box(
                                Modifier
                                    .width(88.dp)
                                    .height(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.linearGradient(b.colors))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF0C1C38) else Color(0xFFE2E8F0),
                                        shape = RoundedCornerShape(12.dp),
                                    ),
                            )
                            Text(
                                b.name,
                                color = if (isSelected) Color(0xFF0C1C38) else Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        }
                    }
                }
            }

            Box(
                Modifier.fillMaxWidth().height(52.dp).clip(CircleShape)
                    .border(1.dp, Color(0xFFFECACA), CircleShape)
                    .clickable { Session.logout(); onLogout() },
                contentAlignment = Alignment.Center,
            ) {
                Text("Cerrar sesión", color = Color(0xFFDC2626), fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
        AppBottomBar(selected = Tab.Perfil, activeColor = Color(0xFF2563EB), onSelect = onTab)
    }
}

@Composable
private fun StatTile(modifier: Modifier, label: String, value: String) {
    val shape = RoundedCornerShape(16.dp)
    Column(modifier.clip(shape).border(1.dp, Color(0xFFEAF0F6), shape).padding(16.dp)) {
        Text(value, color = Color(0xFF1D6FE9), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color(0xFF94A3B8), fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
    }
}