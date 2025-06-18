package com.example.yourday

import YourDayTheme
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yourday.ui.theme.Primary
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

// Активность для отображения splash-экрана при запуске приложения
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            YourDayTheme {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.isStatusBarVisible = false
                }
                // Передаем контекст в SplashScreen
                SplashScreen(context = this)
            }
        }
    }
}

// Composable функция для отображения splash-экрана
@Composable
fun SplashScreen(context: Context) {


    val context= LocalContext.current
    val sharedPref = remember {
        context.getSharedPreferences("authorization", MODE_PRIVATE)
    }
    val isLoggedIn = sharedPref.getBoolean("IS_LOGGED_IN", false)

    Box(
        modifier = Modifier.fillMaxSize().background(Primary),
        contentAlignment = Alignment.Center,

    ) {
        // Вертикальное расположение текста
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp), // Размер изображения
                contentScale = ContentScale.Fit // Масштабирование
            )
        }
    }
    // Переход через 2 секунды
    LaunchedEffect(Unit) {
        delay(2000)
        val intent = if (isLoggedIn) {
            Intent(context, MainActivity::class.java)
        } else {
            Intent(context, RegistrationActivity::class.java)
        }
        context.startActivity(intent)
        (context as? Activity)?.finish()
    }
}


