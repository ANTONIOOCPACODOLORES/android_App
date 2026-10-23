package com.example.englishapp.ui.screens

import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.englishapp.data.Session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF1967D2)

@Composable
fun LoginScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        Spacer(Modifier.height(80.dp))

        Text(
            "Iniciar sesión",
            color = Color(0xFF112440),
            fontSize = 30.sp,
            lineHeight = 36.sp,
            letterSpacing = (-0.75).sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text("Continúa tu aprendizaje", color = Color(0xFF758494), fontSize = 16.sp, lineHeight = 24.sp)

        Spacer(Modifier.height(28.dp))

        AuthField(
            value = email,
            onValueChange = { email = it; error = null },
            placeholder = "Correo electrónico",
            leading = Icons.Outlined.Email,
            keyboardType = KeyboardType.Email,
        )
        Spacer(Modifier.height(16.dp))
        AuthField(
            value = password,
            onValueChange = { password = it; error = null },
            placeholder = "Contraseña",
            leading = Icons.Outlined.Lock,
            keyboardType = KeyboardType.Password,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailing = {
                Icon(
                    imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = "Mostrar u ocultar contraseña",
                    tint = Color(0xFF9AA7B5),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showPassword = !showPassword },
                )
            },
        )

        error?.let { Text(it, color = Color(0xFFDC2626), fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp)) }
        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(2.dp, CircleShape, ambientColor = Color(0xFFBFDBFE), spotColor = Color(0xFFBFDBFE))
                .clip(CircleShape)
                .background(Blue)
                .clickable(enabled = !loading) {
                    if (email.isBlank() || password.isBlank()) {
                        error = "Completa todos los campos."
                    } else {
                        loading = true
                        scope.launch {
                            val err = Session.login(email, password)
                            loading = false
                            if (err == null) onLogin() else error = err
                        }
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(if (loading) "Entrando…" else "Iniciar sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Text(
            "¿Olvidaste tu contraseña?",
            color = Blue,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(top = 12.dp, bottom = 8.dp),
        )

        Spacer(Modifier.height(20.dp))

        // Separador "o"
        Box(Modifier.fillMaxWidth().height(16.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
            Text(
                "o",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                modifier = Modifier.background(Color.White).padding(horizontal = 12.dp),
            )
        }

        Spacer(Modifier.height(20.dp))

        // Botón Google (ícono simplificado, ver nota en README)
        Surface(
            onClick = { Toast.makeText(context, "Google Sign-In: próximamente", Toast.LENGTH_SHORT).show() },
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE5EBF2)),
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth().height(50.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "G",
                    color = Color(0xFF4285F4),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(20.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.width(12.dp))
                Text("Continuar con Google", color = Color(0xFF2B3B4C), fontSize = 14.sp, lineHeight = 20.sp)
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("¿No tienes una cuenta? ", color = Color(0xFF758494), fontSize = 14.sp, lineHeight = 20.sp)
            Text(
                "Regístrate",
                color = Blue,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onRegister),
            )
        }
    }
}

@Composable
internal fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leading: ImageVector,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape)
            .background(Color(0xFFFBFCFD))
            .border(1.dp, Color(0xFFE5EBF2), shape),
    ) {
        Icon(
            imageVector = leading,
            contentDescription = null,
            tint = Color(0xFF9AA7B5),
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp).size(20.dp),
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color(0xFF112440)),
            cursorBrush = SolidColor(Blue),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 49.dp, end = if (trailing != null) 49.dp else 17.dp),
            decorationBox = { inner ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = Color(0xFF9AA7B5), fontSize = 14.sp)
                    }
                    inner()
                }
            },
        )
        if (trailing != null) {
            Box(Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)) { trailing() }
        }
    }
}
