package com.example.yourday.screens

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yourday.R
import com.example.yourday.RegistrationActivity
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.components.ToastDuration
import com.example.yourday.components.ToastManager
import com.example.yourday.components.ToastType
import com.example.yourday.model.ProfileData
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen(
    navController: NavController,
    supabaseHelper: SupabaseHelper
) {
    var profileData by remember { mutableStateOf<ProfileData?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val userId = supabaseHelper.client.auth.currentUserOrNull()?.id
            if (userId != null) {
                val result = supabaseHelper.client.postgrest.from("profiles")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeSingle<ProfileData>()
                profileData = result
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Text(
            text = "Профиль",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                color = Primary
            ),
            textAlign = TextAlign.Center
        )

        LazyColumn {
            // Profile info section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Purple1)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Checkbox(
                                checked = false,
                                onCheckedChange = {},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Primary,
                                    uncheckedColor = DarkBlue
                                ),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = profileData?.username ?: "Loading...",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = DarkBlue
                                )
                            )
                        }
                        Text(
                            text = "Настроение: 💬💬",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                color = DarkBlue
                            ),
                            modifier = Modifier.padding(start = 32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ProfileOptionItem(text = "Хобби", onClick = {
                    // Handle click
                })
                ProfileOptionItem(text = "Привычки", onClick = {
                    // Handle click
                })
                ProfileOptionItem(text = "Лекарства", onClick = {
                    // Handle click
                })
                ProfileOptionItem(
                    text = "Выйти",
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                supabaseHelper.client.auth.signOut()

                                // Clear all local data
                                val sharedPref = context.getSharedPreferences("authorization", MODE_PRIVATE)
                                sharedPref.edit().clear().apply()

                                // Clear any other shared preferences if needed
                                context.getSharedPreferences("YourDayPrefs", MODE_PRIVATE)
                                    .edit()
                                    .clear()
                                    .apply()

                                // Создаем Intent для перехода на RegistrationActivity
                                val intent = Intent(context, RegistrationActivity::class.java).apply {
                                    // Очищаем весь стек активностей
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }

                                // Запускаем новую активность
                                context.startActivity(intent)

                                // Если используете Compose Activity, можно закрыть текущую
                                (context as? Activity)?.finish()

                            } catch (e: Exception) {
                                // Handle logout error
                                withContext(Dispatchers.Main) {
                                    ToastManager.show(
                                        "Ошибка при выходе: ${e.message}",
                                        ToastType.ERROR,
                                        ToastDuration.SHORT
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    color = DarkBlue
                )
            )

            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = Primary,
                    uncheckedColor = Color.Transparent,
                    checkmarkColor = Primary
                ),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}