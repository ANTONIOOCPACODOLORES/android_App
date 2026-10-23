package com.example.englishapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    const val VOCABULARY = "vocabulary"
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
    val intArg = listOf(navArgument("id") { type = NavType.IntType })

    // Si ya había sesión guardada, cargamos su progreso al abrir la app.
    LaunchedEffect(Unit) { if (Session.user != null) Session.loadProgress() }

    NavHost(
        navController = nav,
        startDestination = if (Session.user != null) Routes.HOME else Routes.LOGIN,
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
                onRegister = { nav.navigate(Routes.REGISTER) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = { nav.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } } },
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
                onVocabulary = { nav.navigate(Routes.VOCABULARY) },
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
            PracticeScreen(
                lessonId = entry.arguments?.getInt("id"),
                onClose = { nav.popBackStack() },
                onFinish = { nav.popBackStack(Routes.LESSONS, inclusive = false) },
            )
        }
        composable(Routes.VOCABULARY) {
            VocabularyScreen(onBack = { nav.popBackStack() }, onTab = nav::goToTab)
        }
    }
}
