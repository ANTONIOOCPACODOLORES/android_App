package com.example.englishapp.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishapp.data.Session
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegistered: () -> Unit, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Volver",
            tint = Color(0xFF0F1E36),
            modifier = Modifier.clip(CircleShape).clickable(onClick = onBack).padding(4.dp).size(24.dp),
        )
        Spacer(Modifier.height(40.dp))
        Text("Crear cuenta", color = Color(0xFF112440), fontSize = 30.sp, lineHeight = 36.sp,
            letterSpacing = (-0.75).sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Empieza tu camino en inglés A2", color = Color(0xFF758494), fontSize = 16.sp, lineHeight = 24.sp)
        Spacer(Modifier.height(28.dp))

        AuthField(name, { name = it; error = null }, "Nombre", Icons.Outlined.Person, KeyboardType.Text)
        Spacer(Modifier.height(16.dp))
        AuthField(email, { email = it; error = null }, "Correo electrónico", Icons.Outlined.Email, KeyboardType.Email)
        Spacer(Modifier.height(16.dp))
        AuthField(
            value = password,
            onValueChange = { password = it; error = null },
            placeholder = "Contraseña (mínimo 6 caracteres)",
            leading = Icons.Outlined.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailing = {
                Icon(
                    imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = "Mostrar u ocultar contraseña",
                    tint = Color(0xFF9AA7B5),
                    modifier = Modifier.size(20.dp).clickable { showPassword = !showPassword },
                )
            },
        )

        error?.let {
            Text(it, color = Color(0xFFDC2626), fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
        }
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(CircleShape)
                .background(Color(0xFF1967D2))
                .clickable(enabled = !loading) {
                    error = when {
                        name.isBlank() || email.isBlank() || password.isBlank() -> "Completa todos los campos."
                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Escribe un correo válido."
                        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
                        else -> null
                    }
                    if (error == null) {
                        loading = true
                        scope.launch {
                            val err = Session.register(name, email, password)
                            loading = false
                            if (err == null) onRegistered() else error = err
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(if (loading) "Creando cuenta…" else "Registrarme", color = Color.White,
                fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
