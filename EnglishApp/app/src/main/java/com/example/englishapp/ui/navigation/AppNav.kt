package com.example.englishapp.ui.navigation

import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishapp.data.LessonCatalog
import com.example.englishapp.data.Session
import com.example.englishapp.ui.components.Tab
import com.example.englishapp.ui.screens.HomeScreen
import com.example.englishapp.ui.screens.LessonDetailScreen
import com.example.englishapp.ui.screens.LessonsScreen
import com.example.englishapp.ui.screens.LoginScreen
import com.example.englishapp.ui.screens.PracticeScreen
import com.example.englishapp.ui.screens.ProfileScreen
import com.example.englishapp.ui.screens.ProgressScreen
import com.example.englishapp.ui.screens.RegisterScreen
import com.example.englishapp.ui.screens.VocabularyScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PROGRESS = "progress"
    const val PRACTICE = "practice"
    const val LESSONS = "lessons"
    const val LESSON_DETAIL = "lesson_detail/{id}"
    const val QUIZ = "quiz/{id}"
    const val VOCABULARY = "vocabulary/{topic}"
    const val PROFILE = "profile"
}

private fun NavHostController.goToTab(tab: Tab) {
    val route = when (tab) {
        Tab.Inicio -> Routes.HOME
        Tab.Lecciones -> Routes.LESSONS
        Tab.Practica -> Routes.PRACTICE
        Tab.Perfil -> Routes.PROFILE
    }
    navigate(route) {
        popUpTo(Routes.HOME) { inclusive = false }
        launchSingleTop = true
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val context = LocalContext.current
    // La pantalla inicial se decide UNA sola vez. Si cambiara cada vez que cambia la sesión,
    // la app saltaría sola al inicio justo después de registrarse.
    val startRoute = remember { if (Session.user != null) Routes.HOME else Routes.LOGIN }
    val intArg = listOf(navArgument("id") { type = NavType.IntType })

    // Si ya había sesión guardada, cargamos su progreso al abrir la app.
    LaunchedEffect(Unit) { if (Session.user != null) Session.loadProgress() }

    NavHost(
        navController = nav,
        startDestination = startRoute,
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                onRegister = { nav.navigate(Routes.REGISTER) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = {
                    Toast.makeText(context, "Cuenta creada. Ahora inicia sesión.", Toast.LENGTH_LONG).show()
                    nav.navigate(Routes.LOGIN) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onBack = { nav.popBackStack() },
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onLessons = { nav.navigate(Routes.LESSONS) },
                onPractice = { nav.navigate(Routes.PRACTICE) },
                onProgress = { nav.navigate(Routes.PROGRESS) },
                onTab = nav::goToTab,
            )
        }
        composable(Routes.PROGRESS) { ProgressScreen(onTab = nav::goToTab) }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onTab = nav::goToTab,
                onLogout = { nav.navigate(Routes.LOGIN) { popUpTo(0) } },
            )
        }
        composable(Routes.PRACTICE) {
            // Práctica libre: mezcla preguntas de todas las lecciones (no cambia el % de ninguna).
            PracticeScreen(lessonId = null, onClose = { nav.popBackStack() }, onFinish = { nav.popBackStack() })
        }
        composable(Routes.LESSONS) {
            LessonsScreen(
                onBack = { nav.popBackStack() },
                onLesson = { id -> nav.navigate("lesson_detail/$id") },
                onVocabulary = { topic -> nav.navigate("vocabulary/$topic") },
            )
        }
        composable(Routes.LESSON_DETAIL, arguments = intArg) { entry ->
            val id = entry.arguments?.getInt("id") ?: 1
            LessonDetailScreen(
                lesson = LessonCatalog.first { it.id == id },
                onBack = { nav.popBackStack() },
                onNext = { nav.navigate("quiz/$id") },
            )
        }
        composable(Routes.QUIZ, arguments = intArg) { entry ->
            // Formulario propio de la lección: al terminar guarda el % del usuario.
            val quizId = entry.arguments?.getInt("id")
            PracticeScreen(
                lessonId = quizId,
                onClose = { nav.popBackStack() },
                onFinish = {
                    // Los temas de vocabulario (101, 102, 103) regresan al vocabulario; las lecciones, a la lista.
                    if (quizId != null && quizId >= 100) nav.popBackStack(Routes.VOCABULARY, inclusive = false)
                    else nav.popBackStack(Routes.LESSONS, inclusive = false)
                },
            )
        }
        composable(Routes.VOCABULARY, arguments = listOf(navArgument("topic") { type = NavType.IntType })) { entry ->
            VocabularyScreen(
                initialTopic = entry.arguments?.getInt("topic") ?: 0,
                onBack = { nav.popBackStack() },
                onTab = nav::goToTab,
                onQuiz = { id -> nav.navigate("quiz/$id") },
            )
        }
    }
}