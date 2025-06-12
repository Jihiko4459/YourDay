package com.example.yourday

import YourDayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourDayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val sharedPref = remember {
                        this.getSharedPreferences("authorization", MODE_PRIVATE)
                    }
                    val userId = sharedPref.getString("USER_ID", "")
                    Greeting(
                        name = userId.toString(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    // Обработка кнопки "Назад"
    override fun onBackPressed() {
        super.onBackPressed()
        // Дополнительные действия при необходимости
        finish() // Закрывает текущую Activity
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YourDayTheme {
        Greeting("Android")
    }
}