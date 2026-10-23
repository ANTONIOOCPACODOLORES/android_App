package com.example.englishapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import com.example.englishapp.data.QuestionBank
import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.Session
import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PracticeScreen(lessonId: Int?, onClose: () -> Unit, onFinish: () -> Unit) {
    var attempt by remember { mutableIntStateOf(0) }
    val questions = remember(lessonId, attempt) {
        val pool = if (lessonId == null) QuestionBank else QuestionBank.filter { it.lessonId == lessonId }
        pool.shuffled().take(10)
    }
    var index by remember(lessonId, attempt) { mutableIntStateOf(0) }
    var selected by remember(lessonId, attempt) { mutableStateOf<Int?>(null) }
    var correctCount by remember(lessonId, attempt) { mutableIntStateOf(0) }
    var finished by remember(lessonId, attempt) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (finished) {
        ResultView(lessonId, correctCount, questions.size, onRetry = { attempt++ }, onExit = onFinish)
        return
    }

    val q = questions[index]
    val answered = selected != null
    val isCorrect = selected == q.answer
    val isLast = index == questions.lastIndex

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Cerrar práctica",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(20.dp).clickable(onClick = onClose),
            )
            Spacer(Modifier.width(4.dp))
            Box(Modifier.weight(1f).height(8.dp).clip(CircleShape).background(Color(0xFFF1F5F9))) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth((index + 1) / questions.size.toFloat())
                        .background(Color(0xFF1967D2), CircleShape)
                )
            }
            Text(
                "${index + 1}/${questions.size}",
                color = Color(0xFF64748B), fontSize = 12.sp, lineHeight = 16.sp,
                letterSpacing = 0.3.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
        ) {
            Text(
                "Elige la respuesta correcta",
                color = Color(0xFF1A237E), fontSize = 20.sp, lineHeight = 28.sp,
                letterSpacing = (-0.5).sp, fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(32.dp))
            Text(blankText(q.text), color = Color(0xFF1E293B), fontSize = 22.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                q.options.forEachIndexed { i, opt ->
                    OptionCard(
                        label = "${'a' + i}) $opt",
                        state = when {
                            selected == i && i == q.answer -> OptionState.Correct
                            selected == i -> OptionState.Wrong
                            answered && i == q.answer -> OptionState.Correct
                            else -> OptionState.Default
                        },
                        onClick = {
                            if (!answered) {
                                selected = i
                                if (i == q.answer) correctCount++
                            }
                        },
                    )
                }
            }
        }

        Column(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp)) {
            if (answered) {
                FeedbackCard(
                    isCorrect,
                    if (isCorrect) q.explanation
                    else "La respuesta correcta es ${'a' + q.answer}) ${q.options[q.answer]}. ${q.explanation}",
                )
                Spacer(Modifier.height(12.dp))
            }
            val shape = RoundedCornerShape(16.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .alpha(if (answered) 1f else 0.4f)
                    .shadow(3.dp, shape, ambientColor = Color(0x333B82F6), spotColor = Color(0x333B82F6))
                    .clip(shape)
                    .background(Color(0xFF1976D2))
                    .clickable(enabled = answered) {
                        if (!isLast) {
                            index++
                            selected = null
                        } else {
                            finished = true
                            if (lessonId != null) {
                                scope.launch { Session.saveLessonResult(lessonId, correctCount, questions.size) }
                            }
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    if (isLast) "Ver resultado" else "Siguiente",
                    color = Color.White, fontSize = 16.sp, lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private fun blankText(s: String): AnnotatedString = buildAnnotatedString {
    val parts = s.split("_____")
    parts.forEachIndexed { i, p ->
        append(p)
        if (i < parts.lastIndex) {
            withStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = Color(0xFF94A3B8))) {
                append("\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0")
            }
        }
    }
}

/** Pantalla de resultado con las estadísticas (%) del formulario y del avance de la lección. */
@Composable
private fun ResultView(lessonId: Int?, correct: Int, total: Int, onRetry: () -> Unit, onExit: () -> Unit) {
    val pct = if (total == 0) 0 else correct * 100 / total
    val passed = pct >= Session.PASS_PERCENT
    val saved = lessonId?.let { Session.progress[it] }
    val lessonTitle = lessonId?.let { id -> LessonCatalog.firstOrNull { it.id == id }?.title }
    val ringColor = if (passed) Color(0xFF10B981) else Color(0xFF1967D2)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            if (passed) "¡Lección aprobada!" else "¡Sigue practicando!",
            color = Color(0xFF102A43), fontSize = 24.sp, fontWeight = FontWeight.Bold,
        )
        Text(lessonTitle ?: "Práctica libre", color = Color(0xFF94A3B8), fontSize = 14.sp)
        Spacer(Modifier.height(28.dp))

        Box(Modifier.size(176.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val stroke = 14.dp.toPx()
                val inset = stroke / 2
                val arc = Size(size.width - stroke, size.height - stroke)
                drawArc(Color(0xFFE2E8F0), 0f, 360f, false, Offset(inset, inset), arc, style = Stroke(stroke))
                drawArc(ringColor, -90f, 360f * pct / 100f, false, Offset(inset, inset), arc,
                    style = Stroke(stroke, cap = StrokeCap.Round))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$pct%", color = Color(0xFF0C1C38), fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("$correct de $total", color = Color(0xFF94A3B8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(24.dp))

        if (saved != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ResultStat(Modifier.weight(1f), "Mejor resultado", "${saved.best}%")
                ResultStat(Modifier.weight(1f), "Intentos", "${saved.attempts}")
                ResultStat(Modifier.weight(1f), "Estado", if (saved.best >= Session.PASS_PERCENT) "Aprobada" else "En curso")
            }
            Text(
                "Necesitas ${Session.PASS_PERCENT}% para aprobar la lección.",
                color = Color(0xFF94A3B8), fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp),
            )
        } else if (lessonId == null) {
            Text(
                "La práctica libre no cambia el % de tus lecciones.",
                color = Color(0xFF94A3B8), fontSize = 12.sp,
            )
        }

        Spacer(Modifier.weight(1f))
        val shape = RoundedCornerShape(16.dp)
        Box(
            Modifier.fillMaxWidth().height(52.dp).clip(shape).background(Color(0xFF1976D2)).clickable(onClick = onRetry),
            contentAlignment = Alignment.Center,
        ) { Text("Reintentar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(10.dp))
        Box(
            Modifier.fillMaxWidth().height(48.dp).clip(shape).clickable(onClick = onExit),
            contentAlignment = Alignment.Center,
        ) {
            Text(if (lessonId != null) "Volver a lecciones" else "Salir",
                color = Color(0xFF1976D2), fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ResultStat(modifier: Modifier, label: String, value: String) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier.clip(shape).border(1.dp, Color(0xFFEAF0F6), shape).padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, color = Color(0xFF1D6FE9), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color(0xFF94A3B8), fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 2.dp))
    }
}

private enum class OptionState { Default, Correct, Wrong }

@Composable
private fun OptionCard(label: String, state: OptionState, onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val (bg, borderColor, textColor, weight, borderWidth) = when (state) {
        OptionState.Default -> OptionStyle(Color.White, Color(0xFFE2E8F0), Color(0xFF334155), FontWeight.Normal, 1.dp)
        OptionState.Correct -> OptionStyle(Color(0xFFF1F8E9), Color(0xFF4CAF50), Color(0xFF2E7D32), FontWeight.Bold, 2.dp)
        OptionState.Wrong -> OptionStyle(Color(0xFFFFEBEE), Color(0xFFEF5350), Color(0xFFC62828), FontWeight.Bold, 2.dp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape)
            .clip(shape)
            .background(bg)
            .border(borderWidth, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 17.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = textColor, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = weight)
        when (state) {
            OptionState.Correct -> Icon(Icons.Filled.Check, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
            OptionState.Wrong -> Icon(Icons.Filled.Close, null, tint = Color(0xFFEF5350), modifier = Modifier.size(20.dp))
            else -> {}
        }
    }
}

private data class OptionStyle(
    val bg: Color,
    val border: Color,
    val text: Color,
    val weight: FontWeight,
    val borderWidth: androidx.compose.ui.unit.Dp,
)

@Composable
private fun FeedbackCard(correct: Boolean, message: String) {
    val shape = RoundedCornerShape(16.dp)
    val bg = if (correct) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val line = if (correct) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
    val strong = if (correct) Color(0xFF2E7D32) else Color(0xFFC62828)
    val soft = if (correct) Color(0xFF388E3C) else Color(0xFFD32F2F)
    val dot = if (correct) Color(0xFF4CAF50) else Color(0xFFEF5350)

    Column(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bg)
            .border(1.dp, line, shape)
            .padding(17.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(20.dp).background(dot, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    if (correct) Icons.Filled.Check else Icons.Filled.Close,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                if (correct) "¡Correcto!" else "Incorrecto",
                color = strong,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        Text(
            message,
            color = soft,
            fontSize = 12.sp,
            lineHeight = 19.5.sp,
            modifier = Modifier.padding(start = 28.dp, top = 3.dp),
        )
    }
}
