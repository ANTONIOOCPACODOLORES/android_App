package com.example.englishapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class LessonData(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val uses: List<String>,
    val structureTitle: String,
    val structureLines: List<String>,
    val examples: List<String>,   // **texto** = negritas
)

/** Cada lección tiene su propio formulario (5 preguntas). */
data class QuizQuestion(
    val lessonId: Int,
    val text: String,             // "_____" marca el espacio en blanco
    val options: List<String>,
    val answer: Int,
    val explanation: String,
)

val LessonCatalog = listOf(
    LessonData(
        1, "Present Simple", "Habla de rutinas y hábitos.", Icons.Filled.CalendarMonth, Color(0xFFFBBF24),
        listOf("Hablar de hábitos o rutinas.", "Verdades generales.", "Hechos que no cambian."),
        "Sujeto + verbo base",
        listOf("I work", "You work", "He/She/It **works**", "We/You/They **work**"),
        listOf("**I get up** at 7 a.m.", "She **likes** coffee.", "They **play** soccer."),
    ),
    LessonData(
        2, "Present Continuous", "Habla de acciones en progreso.", Icons.Filled.Schedule, Color(0xFF6366F1),
        listOf("Acciones que ocurren ahora mismo.", "Situaciones temporales.", "Planes cercanos."),
        "Sujeto + am/is/are + verbo-ing",
        listOf("I **am** working", "He/She/It **is** working", "You/We/They **are** working"),
        listOf("I **am studying** English now.", "She **is watching** TV.", "They **are playing** outside."),
    ),
    LessonData(
        3, "Past Simple", "Habla de acciones pasadas.", Icons.Filled.FastRewind, Color(0xFFF59E0B),
        listOf("Acciones terminadas en el pasado.", "Secuencias de hechos pasados.", "Con: yesterday, last week, in 2020."),
        "Sujeto + verbo en pasado (-ed o irregular)",
        listOf("I **worked** yesterday", "She **went** to school", "They **saw** a movie"),
        listOf("I **visited** my aunt last week.", "We **ate** pizza on Friday.", "He **didn't go** to work."),
    ),
    LessonData(
        4, "Future (will / going to)", "Habla de planes y decisiones.", Icons.Filled.Event, Color(0xFF06B6D4),
        listOf("will: decisiones en el momento y promesas.", "going to: planes e intenciones.", "going to: predicciones con evidencia."),
        "will + verbo base / am-is-are going to + verbo",
        listOf("I **will** call you", "She **is going to** travel", "They **are going to** study"),
        listOf("I'm thirsty. I **will** get water.", "We **are going to** visit Paris.", "It **is going to** rain."),
    ),
    LessonData(
        5, "Adjectives", "Describe personas, lugares y cosas.", Icons.Filled.Star, Color(0xFFFBBF24),
        listOf("Describir personas, lugares y cosas.", "Van después de 'be': She is tall.", "Van antes del sustantivo: a big house."),
        "Sujeto + be + adjetivo",
        listOf("She is **tall**", "The house is **big**", "They are **happy**"),
        listOf("My city is **small** but **beautiful**.", "He is a **kind** person.", "The food is **delicious**."),
    ),
)

val QuestionBank = listOf(
    // 1 - Present Simple
    QuizQuestion(1, "I _____ English every day.", listOf("study", "studies", "studying", "studied"), 0,
        "El verbo 'study' se usa con I en Present Simple."),
    QuizQuestion(1, "She _____ coffee in the morning.", listOf("drink", "drinks", "drinking", "drank"), 1,
        "Con he/she/it el verbo lleva -s: drinks."),
    QuizQuestion(1, "They _____ soccer on Sundays.", listOf("plays", "play", "playing", "played"), 1,
        "Con they usamos el verbo base: play."),
    QuizQuestion(1, "He _____ up at 7 a.m.", listOf("get", "gets", "getting", "got"), 1,
        "Con he/she/it el verbo lleva -s: gets."),
    QuizQuestion(1, "We _____ work at six.", listOf("doesn't", "don't", "not", "no"), 1,
        "La negación con we es don't + verbo base."),
    // 2 - Present Continuous
    QuizQuestion(2, "They _____ TV right now.", listOf("watch", "watches", "are watching", "watched"), 2,
        "'Right now' indica acción en progreso: are + verbo-ing."),
    QuizQuestion(2, "I _____ a book at the moment.", listOf("am reading", "reads", "read", "will read"), 0,
        "'At the moment' pide Present Continuous: am + reading."),
    QuizQuestion(2, "She _____ in the kitchen now.", listOf("is cooking", "cooks", "cook", "cooked"), 0,
        "Acción en progreso con she: is + cooking."),
    QuizQuestion(2, "We _____ for the bus.", listOf("is waiting", "are waiting", "waits", "wait"), 1,
        "Con we usamos are + verbo-ing."),
    QuizQuestion(2, "Look! It _____ outside.", listOf("rains", "rained", "is raining", "rain"), 2,
        "'Look!' señala algo que pasa ahora: is raining."),
    // 3 - Past Simple
    QuizQuestion(3, "We _____ to the beach last summer.", listOf("go", "goes", "went", "going"), 2,
        "'Last summer' es pasado y 'go' es irregular: went."),
    QuizQuestion(3, "She _____ her homework yesterday.", listOf("finish", "finished", "finishes", "finishing"), 1,
        "Verbo regular en pasado: agregamos -ed."),
    QuizQuestion(3, "I _____ a movie last night.", listOf("see", "seen", "saw", "sees"), 2,
        "'See' es irregular: saw."),
    QuizQuestion(3, "He _____ go to work on Monday.", listOf("didn't", "don't", "doesn't", "wasn't"), 0,
        "La negación en pasado es didn't + verbo base."),
    QuizQuestion(3, "They _____ pizza on Friday.", listOf("eat", "eats", "ate", "eaten"), 2,
        "'Eat' es irregular: ate."),
    // 4 - Future
    QuizQuestion(4, "Look at the clouds! It _____ rain.", listOf("is going to", "are going to", "goes to", "went to"), 0,
        "Predicción con evidencia presente: is going to."),
    QuizQuestion(4, "I'm thirsty. I _____ get some water.", listOf("will", "was", "went", "am"), 0,
        "Decisión en el momento: will + verbo base."),
    QuizQuestion(4, "We _____ visit Paris next year.", listOf("are going to", "is going to", "will to", "going"), 0,
        "Plan con we: are going to + verbo."),
    QuizQuestion(4, "I think she _____ win the game.", listOf("will", "was", "did", "does"), 0,
        "Opinión o predicción: will + verbo base."),
    QuizQuestion(4, "They _____ travel by train tomorrow.", listOf("are going to", "is going to", "going to", "will going"), 0,
        "Plan con they: are going to + verbo."),
    // 5 - Adjectives
    QuizQuestion(5, "My city is very _____.", listOf("beautifully", "beautiful", "beauty", "beautify"), 1,
        "Después de 'be' usamos un adjetivo: beautiful."),
    QuizQuestion(5, "He is a _____ man.", listOf("kindly", "kind", "kindness", "kinds"), 1,
        "Antes del sustantivo va un adjetivo: kind."),
    QuizQuestion(5, "The food is _____.", listOf("deliciously", "delicious", "deliciousness", "delicates"), 1,
        "Después de 'be' va un adjetivo: delicious."),
    QuizQuestion(5, "She has a _____ dog.", listOf("small brown", "brown small", "smalls", "browns"), 0,
        "En inglés el tamaño va antes del color: small brown."),
    QuizQuestion(5, "They are very _____ today.", listOf("happily", "happy", "happiness", "happies"), 1,
        "Después de 'be' usamos un adjetivo: happy."),
)
