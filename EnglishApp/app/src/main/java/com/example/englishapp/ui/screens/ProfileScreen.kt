package com.example.englishapp.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.Session
import com.example.englishapp.ui.components.AppBottomBar
import com.example.englishapp.ui.components.Tab

@Composable
fun ProfileScreen(onTab: (Tab) -> Unit, onLogout: () -> Unit) {
    val user = Session.user
    val shape = RoundedCornerShape(16.dp)
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

            Row(
                Modifier.fillMaxWidth().clip(shape).border(1.dp, Color(0xFFEAF0F6), shape).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(56.dp).clip(CircleShape).background(Color(0xFFDBEAFE)), contentAlignment = Alignment.Center) {
                    Text((user?.name?.firstOrNull() ?: 'E').uppercase(), color = Color(0xFF1D6FE9),
                        fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Column(Modifier.padding(start = 14.dp)) {
                    Text(user?.name ?: "Estudiante", color = Color(0xFF0C1C38), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(user?.email ?: "", color = Color(0xFF94A3B8), fontSize = 13.sp)
                }
            }

            Text(
                if (Session.isCloud) "☁  Cuenta en la nube (Firebase)" else "📱  Cuenta local en este dispositivo",
                color = Color(0xFF475569), fontSize = 12.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatTile(Modifier.weight(1f), "Progreso general", "${Session.overall}%")
                StatTile(Modifier.weight(1f), "Lecciones aprobadas", "${Session.completed} / ${LessonCatalog.size}")
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
