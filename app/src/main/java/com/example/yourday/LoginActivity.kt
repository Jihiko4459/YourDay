package com.example.yourday

import YourDayTheme
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.api.SupabaseHelper.AuthResult
import com.example.yourday.components.GlobalToast
import com.example.yourday.components.ToastManager
import com.example.yourday.components.ToastType
import com.example.yourday.ui.theme.Black
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

// Активность для экрана входа в приложение
class LoginActivity : ComponentActivity() {
    private val authHelper by lazy { SupabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            try {
                authHelper.checkSessionValidity()
            } catch (e: Exception) {
                Log.e("Auth", "Session check failed", e)
            }
            setContent {
                YourDayTheme {
                    var isLoading by remember { mutableStateOf(false) }

                    LoginScreen(
                        onLoginClick = { email, password, rememberMe ->
                            lifecycleScope.launch {
                                val cleanEmail = email.trim()
                                if (cleanEmail.isEmpty()) {
                                    ToastManager.show(
                                        "Email не может быть пустым",
                                        ToastType.ERROR
                                    )
                                    return@launch
                                }

                                if (!isValidEmail(cleanEmail)) {
                                    ToastManager.show(
                                        "Неправильно введен email",
                                        ToastType.ERROR
                                    )
                                    return@launch
                                }

                                // Валидация пароля перед входом
                                if (!validatePassword(password)) {
                                    return@launch
                                }

                                isLoading = true
                                handleLogin(cleanEmail, password, rememberMe)
                                isLoading = false
                            }
                        },
                        onForgotPasswordClick = {
                            // Handle forgot password
                        }
                    )
                }
            }
        }
    }
    // Обработка входа пользователя
    private suspend fun handleLogin(email: String, password: String, rememberMe: Boolean) {
        try {
            val result = authHelper.signInWithEmail(email, password)
            when (result) {
                is AuthResult.Success -> {

                    if (rememberMe==true) {
                        // Сохраняем состояние входа
                        getSharedPreferences("authorization", MODE_PRIVATE)
                            .edit()
                            .putBoolean("IS_LOGGED_IN", true)
                            .putString("USER_ID", result.userId)
                            .apply()
                    }

                    authHelper.updateUserProfile(result.userId)
                    intentToMainActivity()
                }
                is AuthResult.Failure -> {
                    ToastManager.show(result.error.message ?: "Ошибка входа", ToastType.ERROR)
                }
            }
        } catch (e: Exception) {
            handleLoginError(e)
        }
    }
    // Переход на главный экран
    private fun intentToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Обработка ошибок входа
    private fun handleLoginError(error: Exception) {
        val message = when {
            error.message?.contains("Invalid login credentials") == true -> {
                "Неверный email или пароль"
            }
            else -> "Ошибка входа: ${error.localizedMessage ?: "Неизвестная ошибка"}"
        }
        showToast(message)
        Log.e("LOGIN", "Error details", error)
    }

    // Проверка валидности email
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Показать Toast сообщение
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Валидация пароля
    private fun validatePassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                ToastManager.show("Пароль не может быть пустым", ToastType.ERROR)
                false
            }
            password.length < 8 -> {
                ToastManager.show("Пароль должен содержать минимум 8 символов", ToastType.ERROR)
                false
            }
            password.length > 32 -> {
                ToastManager.show("Пароль не должен превышать 32 символа", ToastType.ERROR)
                false
            }
            !password.any { it.isDigit() } -> {
                ToastManager.show("Пароль должен содержать хотя бы одну цифру", ToastType.ERROR)
                false
            }
            !password.any { it.isLetter() } -> {
                ToastManager.show("Пароль должен содержать хотя бы одну букву", ToastType.ERROR)
                false
            }
            !password.any { !it.isLetterOrDigit() } -> {
                ToastManager.show("Пароль должен содержать хотя бы один специальный символ", ToastType.ERROR)
                false
            }
            password == password.lowercase() -> {
                ToastManager.show("Пароль должен содержать хотя бы одну заглавную букву", ToastType.ERROR)
                false
            }
            else -> true
        }
    }

    // Обработка нажатия кнопки "Назад"
    override fun onBackPressed() {
        super.onBackPressed()
        finish() // Закрывает текущую Activity
    }
}

// Экран входа
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String, rememberMe: Boolean) -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val hasEmail = email.isNotEmpty()
    val hasPassword = password.isNotEmpty()

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Вход",
                    style = TextStyle(
                        fontSize = 40.sp,
                        lineHeight = 30.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        color = Primary
                    ),
                    modifier = Modifier.padding(top = 46.dp)
                )

                Text(
                    text = "Пожалуйста, заполните поля для входа",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = Gray1
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .width(250.dp)
                            .aspectRatio(1f / 1f),
                        contentScale = ContentScale.Fit,
                    )
                }

                Text(
                    text = "Email",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue
                    )
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "example@mail.com",
                            color = Gray1,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular))
                        )
                    },
                    textStyle = TextStyle(
                        color = DarkBlue,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .border(
                            width = 2.dp,
                            color = if (hasEmail) Primary else Gray1,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = "Пароль",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue
                    )
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Введите пароль",
                            color = Gray1,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular))
                        )
                    },
                    textStyle = TextStyle(
                        color = DarkBlue,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.eye_opened
                                    else R.drawable.eye_clossed
                                ),
                                contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                                modifier = Modifier.size(24.dp),
                                tint = if (hasPassword) Primary else Gray1
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .border(
                            width = 2.dp,
                            color = if (hasPassword) Primary else Gray1,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconToggleButton(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        ) {
                            Image(
                                painter = painterResource(
                                    if (rememberMe) R.drawable.check2
                                    else R.drawable.check1
                                ),
                                contentDescription = if (rememberMe) "Checked" else "Unchecked",
                                modifier = Modifier.size(24.dp),
                                colorFilter = null // Отключаем стандартный tint
                            )
                        }
                        Text(
                            text = "Запомнить меня",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                color = DarkBlue
                            )
                        )
                    }

                    TextButton(onClick = onForgotPasswordClick) {
                        Text(
                            text = "Забыли пароль?",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                color = Primary
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onLoginClick(email, password, rememberMe) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .background(Primary, RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Войти",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.9.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            textAlign = TextAlign.Center,
                            color = if (isDarkTheme) White else Black
                        )
                    )
                }
            }
        }

        GlobalToast()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    YourDayTheme {
    }
}