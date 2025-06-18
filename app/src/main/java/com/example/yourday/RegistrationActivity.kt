package com.example.yourday

import GetIsDarkTheme
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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

val isDarkTheme: Boolean
    @Composable
    get() = GetIsDarkTheme()

class RegistrationActivity : ComponentActivity() {
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

                    RegistrationScreen(onRegisterClick = { email, password, confirmPassword ->
                            lifecycleScope.launch {
                                val cleanEmail = email.trim()
                                if (cleanEmail.isEmpty()) {
                                    ToastManager.show(
                                        "Email не может быть пустым",
                                        ToastType.ERROR
                                    )
                                } else if (!isValidEmail(cleanEmail)) {
                                    ToastManager.show(
                                        "Неправильно введен email",
                                        ToastType.ERROR
                                    )
                                } else if (!validatePassword(password, confirmPassword)) {
                                    // Сообщение об ошибке уже показано в validatePassword
                                } else {
                                    isLoading = true
                                    handleRegistration(cleanEmail, password)
                                    isLoading = false
                                }
                            }
                        },
                        onLoginClick = {
                            intentToLogin()
                        }
                    )
                }
            }
        }
    }

    private suspend fun handleRegistration(email: String, password: String) {
        try {
            val result = authHelper.signUpWithEmail(email, password)
            when (result) {
                is AuthResult.Success -> {
                    // Переходим к следующему шагу с ID пользователя
                    intentToLastStepToRegistration(result.userId)
                }
                is AuthResult.Failure -> {
                    ToastManager.show(result.error.message ?: "Ошибка регистрации", ToastType.ERROR)
                }
            }
        } catch (e: Exception) {
            handleRegistrationError(e)
        }
    }

    private fun intentToLastStepToRegistration(userId: String) {
        startActivity(Intent(this, LastStepToRegistrationActivity::class.java).apply {
            putExtra("USER_ID", userId)
        })
    }

    private fun handleRegistrationError(error: Exception) {
        val message = when {
            error.message?.contains("User from sub claim in JWT does not exist") == true -> {
                "Сессия устарела. Пожалуйста, попробуйте снова"
            }
            error.message?.contains("already registered") == true -> "Email уже зарегистрирован"
            else -> "Ошибка регистрации: ${error.localizedMessage ?: "Неизвестная ошибка"}"
        }
        showToast(message)
        Log.e("REGISTRATION", "Error details", error)
    }

    private fun intentToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
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
            password != confirmPassword -> {
                ToastManager.show("Пароли не совпадают", ToastType.ERROR)
                false
            }
            password == password.lowercase() -> {
                ToastManager.show("Пароль должен содержать хотя бы одну заглавную букву", ToastType.ERROR)
                false
            }
            else -> true
        }
    }
}

@Composable
fun RegistrationScreen(
    onRegisterClick: (email: String, password: String, confirmPassword: String) -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordAgainVisible by remember { mutableStateOf(false) }

    val hasText1 = email.isNotEmpty()
    val hasText2 = password.isNotEmpty()
    val hasText3 = passwordAgain.isNotEmpty()

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
                    text = "Регистрация",
                    style = TextStyle(
                        fontSize = 40.sp,
                        lineHeight = 30.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                        textAlign = TextAlign.Center,
                        color = Primary
                    ),
                    modifier = Modifier.padding(top = 46.dp)
                )

                Text(
                    text = "Пожалуйста, заполните email для регистрации",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = Gray1
                    )
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
                            color = if (hasText1) Primary else Gray1,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))

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
                            fontFamily = FontFamily(Font(R.font.roboto_regular)))

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
                        imeAction = ImeAction.Next
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
                                tint = if (hasText2) Primary else Gray1 // Added color change
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .border(
                            width = 2.dp,
                            color = if (hasText2) Primary else Gray1,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))

                Text(
                    text = "Повторите пароль",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue
                    )
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))
                OutlinedTextField(
                    value = passwordAgain,
                    onValueChange = { passwordAgain = it },
                    placeholder = {
                        Text(
                            "Повторите введённый пароль",
                            color = Gray1,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)))
                    },
                    textStyle = TextStyle(
                        color = DarkBlue,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordAgainVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordAgainVisible = !passwordAgainVisible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordAgainVisible) R.drawable.eye_opened
                                    else R.drawable.eye_clossed
                                ),
                                contentDescription = if (passwordAgainVisible) "Скрыть пароль" else "Показать пароль",
                                modifier = Modifier.size(24.dp),
                                tint = if (hasText3) Primary else Gray1 // Added color change
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .border(
                            width = 2.dp,
                            color = if (hasText3) Primary else Gray1,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                            onRegisterClick(email, password, passwordAgain)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .background(Primary, RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Создать аккаунт",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 16.9.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            textAlign = TextAlign.Center,
                            color = if (isDarkTheme) White else Black
                        )
                    )
                }

                TextButton(
                    onClick = onLoginClick,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "У меня уже есть аккаунт",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = Gray1
                        )
                    )
                }
            }
        }

        GlobalToast()

    }
}

@Preview
@Composable
fun GreetingPreview2() {
    YourDayTheme {
    }
}