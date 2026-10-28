package com.example.englishapp.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random

/** Tema de vocabulario. Su id también se usa como "id de lección" para guardar el % del mini formulario. */
data class VocabTopic(
    val id: Int,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val bg: Color,
)

data class VocabWord(val en: String, val es: String, val topicId: Int, val example: String)

const val TOPIC_TRAVEL = 101
const val TOPIC_FOOD = 102
const val TOPIC_WORK = 103

val VocabTopics = listOf(
    VocabTopic(TOPIC_TRAVEL, "Viajes", "Aeropuerto, hotel y turismo", Icons.Filled.Flight, Color(0xFF0284C7), Color(0xFFE0F2FE)),
    VocabTopic(TOPIC_FOOD, "Comida", "Restaurante, comidas y bebidas", Icons.Filled.Restaurant, Color(0xFFEA580C), Color(0xFFFFEDD5)),
    VocabTopic(TOPIC_WORK, "Trabajo", "Oficina, reuniones y empleo", Icons.Filled.Work, Color(0xFF4F46E5), Color(0xFFE0E7FF)),
)

val VocabWords = listOf(
    // Viajes
    VocabWord("airport", "aeropuerto", TOPIC_TRAVEL, "We arrive at the airport at six."),
    VocabWord("hotel", "hotel", TOPIC_TRAVEL, "The hotel is near the beach."),
    VocabWord("ticket", "boleto", TOPIC_TRAVEL, "I need a ticket to Madrid."),
    VocabWord("passport", "pasaporte", TOPIC_TRAVEL, "Don't forget your passport."),
    VocabWord("luggage", "equipaje", TOPIC_TRAVEL, "My luggage is very heavy."),
    VocabWord("flight", "vuelo", TOPIC_TRAVEL, "The flight leaves at noon."),
    VocabWord("reservation", "reservación", TOPIC_TRAVEL, "I have a reservation for two."),
    VocabWord("map", "mapa", TOPIC_TRAVEL, "Can I see the map, please?"),
    VocabWord("beach", "playa", TOPIC_TRAVEL, "We walk on the beach every morning."),
    VocabWord("city", "ciudad", TOPIC_TRAVEL, "This city is very beautiful."),
    VocabWord("train station", "estación de tren", TOPIC_TRAVEL, "The train station is close."),
    VocabWord("tourist", "turista", TOPIC_TRAVEL, "She is a tourist in Mexico."),
    // Comida
    VocabWord("restaurant", "restaurante", TOPIC_FOOD, "This restaurant is very good."),
    VocabWord("menu", "menú", TOPIC_FOOD, "Can I have the menu, please?"),
    VocabWord("breakfast", "desayuno", TOPIC_FOOD, "I eat breakfast at seven."),
    VocabWord("lunch", "almuerzo", TOPIC_FOOD, "We have lunch at two."),
    VocabWord("dinner", "cena", TOPIC_FOOD, "Dinner is ready."),
    VocabWord("water", "agua", TOPIC_FOOD, "I drink water every day."),
    VocabWord("bread", "pan", TOPIC_FOOD, "She buys fresh bread."),
    VocabWord("chicken", "pollo", TOPIC_FOOD, "I like chicken with rice."),
    VocabWord("rice", "arroz", TOPIC_FOOD, "We eat rice for lunch."),
    VocabWord("fruit", "fruta", TOPIC_FOOD, "Fruit is healthy."),
    VocabWord("vegetables", "verduras", TOPIC_FOOD, "He eats vegetables every day."),
    VocabWord("bill", "cuenta", TOPIC_FOOD, "Can we have the bill, please?"),
    // Trabajo
    VocabWord("job", "empleo", TOPIC_WORK, "She has a new job."),
    VocabWord("office", "oficina", TOPIC_WORK, "My office is downtown."),
    VocabWord("boss", "jefe", TOPIC_WORK, "My boss is very kind."),
    VocabWord("meeting", "reunión", TOPIC_WORK, "We have a meeting at ten."),
    VocabWord("salary", "salario", TOPIC_WORK, "The salary is good."),
    VocabWord("colleague", "compañero de trabajo", TOPIC_WORK, "My colleague helps me a lot."),
    VocabWord("interview", "entrevista", TOPIC_WORK, "I have a job interview tomorrow."),
    VocabWord("email", "correo electrónico", TOPIC_WORK, "I send an email every morning."),
    VocabWord("schedule", "horario", TOPIC_WORK, "What is your work schedule?"),
    VocabWord("project", "proyecto", TOPIC_WORK, "We finish the project on Friday."),
    VocabWord("company", "empresa", TOPIC_WORK, "He works for a big company."),
    VocabWord("deadline", "fecha límite", TOPIC_WORK, "The deadline is next week."),
)

/** Ids que cuentan para el progreso: las lecciones de gramática + los temas de vocabulario. */
val ProgressIds: List<Int> = LessonCatalog.map { it.id } + VocabTopics.map { it.id }

fun isVocabTopic(id: Int): Boolean = VocabTopics.any { it.id == id }

/** Mini formulario del tema: una pregunta por palabra, en inglés→español o español→inglés. */
fun vocabQuestions(topicId: Int): List<QuizQuestion> {
    val words = VocabWords.filter { it.topicId == topicId }
    return words.map { w ->
        val others = words.filter { it.en != w.en }.shuffled().take(3)
        if (Random.nextBoolean()) {
            val options = (others.map { it.es } + w.es).shuffled()
            QuizQuestion(
                topicId, "¿Qué significa \"${w.en}\"?", options, options.indexOf(w.es),
                "\"${w.en}\" significa \"${w.es}\". Ejemplo: ${w.example}", w.en,
            )
        } else {
            val options = (others.map { it.en } + w.en).shuffled()
            QuizQuestion(
                topicId, "¿Cómo se dice \"${w.es}\" en inglés?", options, options.indexOf(w.en),
                "\"${w.es}\" se dice \"${w.en}\". Ejemplo: ${w.example}", w.en,
            )
        }
    }
}