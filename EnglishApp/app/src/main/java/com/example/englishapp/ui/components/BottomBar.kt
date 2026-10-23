package com.example.englishapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class Tab(val label: String, val icon: ImageVector, val activeIcon: ImageVector) {
    Inicio("Inicio", Icons.Outlined.Home, Icons.Filled.Home),
    Lecciones("Lecciones", Icons.Outlined.MenuBook, Icons.Filled.MenuBook),
    Practica("Práctica", Icons.Outlined.CheckCircle, Icons.Filled.CheckCircle),
    Perfil("Perfil", Icons.Outlined.Person, Icons.Filled.Person),
}

/** Barra de navegación inferior usada en Home, Mi progreso y Vocabulario. */
@Composable
fun AppBottomBar(
    selected: Tab,
    activeColor: Color,
    onSelect: (Tab) -> Unit,
) {
    val inactive = Color(0xFF94A3B8)
    Column(Modifier.fillMaxWidth().background(Color.White)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tab.entries.forEach { tab ->
                val active = tab == selected
                val color = if (active) activeColor else inactive
                Column(
                    modifier = Modifier.clickable { onSelect(tab) }.padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = if (active) tab.activeIcon else tab.icon,
                        contentDescription = tab.label,
                        tint = color,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = tab.label,
                        color = color,
                        fontSize = 10.sp,
                        lineHeight = 15.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}
