package com.example.englishapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.englishapp.data.Session
import com.example.englishapp.ui.navigation.AppNav
import com.example.englishapp.ui.theme.EnglishAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            EnglishAppTheme {
                AppNav()
            }
        }
    }
}
