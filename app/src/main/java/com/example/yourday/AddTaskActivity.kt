package com.example.yourday

import YourDayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.screens.AddTaskScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class AddTaskActivity : ComponentActivity() {
    private val authHelper by lazy { SupabaseHelper() }
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            YourDayDatabase::class.java,
            "notes.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            YourDayTheme {

                val systemUiController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = darkTheme
                    )

                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme,
                        navigationBarContrastEnforced = true
                    )
                }


                AddTaskScreen(
                    database = database,
                    onTaskCreated = { finish() } // Закрываем активность после создания
                )

            }
        }
    }
}