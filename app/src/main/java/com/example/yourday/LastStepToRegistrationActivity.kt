package com.example.yourday

import YourDayTheme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.components.GlobalToast
import com.example.yourday.components.ToastManager
import com.example.yourday.components.ToastType
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

// Активность для завершающего шага регистрации пользователя
class LastStepToRegistrationActivity : ComponentActivity() {
    private val authHelper by lazy { SupabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Получаем данные из Intent
        val userId = intent.getStringExtra("USER_ID") ?: run {
            ToastManager.show("Ошибка: ID пользователя не получен", ToastType.ERROR)
            finish()
            return
        }

        Log.d("LastStepToRegistration", "User ID: $userId")



        enableEdgeToEdge()
        // Проверяем текущего пользователя
        lifecycleScope.launch {
            try {
                setupUI(userId)
            } catch (e: Exception) {
                Log.e("LastStep", "Error checking user", e)
                finish()
            }
        }
    }

    private fun setupUI(userId: String) {
        setContent {
            YourDayTheme {
                LastStepToRegistrationScreen(
                    onRegisterClick = { nickname, birthDate, termsAccepted ->
                        handleProfileUpdate(userId, nickname, birthDate, termsAccepted)
                    },
                    onOpenPrivacyPolicy = {
                        openPrivacyPolicy(this@LastStepToRegistrationActivity)
                    }
                )
            }
        }
    }

    private fun handleProfileUpdate(userId: String, nickname: String, birthDate: String, termsAccepted: Boolean) {
        lifecycleScope.launch {
            if (!termsAccepted) {
                ToastManager.show("Необходимо принять условия", ToastType.ERROR)
                return@launch
            }

            if (birthDate.length != 8) {
                ToastManager.show("Введите полную дату рождения", ToastType.ERROR)
                return@launch
            }

            try {
                val success = authHelper.insertUserProfile(
                    userId = userId,
                    username = nickname,
                    birthDate = birthDate
                )

                if (success) {


                    navigateToMain(userId)
                } else {
                    ToastManager.show("Ошибка сохранения профиля", ToastType.ERROR)
                }
            } catch (e: Exception) {
                ToastManager.show("Ошибка: ${e.message}", ToastType.ERROR)
                Log.e("ProfileUpdate", "Error details", e)
            }
        }
    }

    private fun navigateToMain(userId: String) {
        // Сохраняем состояние входа
        getSharedPreferences("authorization", MODE_PRIVATE)
            .edit()
            .putString("USER_ID", userId)
            .putBoolean("IS_LOGGED_IN", true)
            .apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
// Экран последнего шага регистрации
@Composable
fun LastStepToRegistrationScreen(
    onRegisterClick: (nickname: String, birthDate: String, termsAccepted: Boolean) -> Unit,
    onOpenPrivacyPolicy:()-> Unit
) {
    var nick by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    val hasText1 = nick.isNotEmpty()
    val hasText2 = dateOfBirth.isNotEmpty()

    var termsAccepted by remember { mutableStateOf(false) }
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
                    text = "Последний шаг",
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
                    text = "Осталось заполнить следующие данные о себе",
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
                            .fillMaxWidth(0.7f)
                            .aspectRatio(1f / 0.8f),
                        contentScale = ContentScale.Fit,
                    )
                }

                Text(
                    text = "Ник",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue
                    )
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))
                OutlinedTextField(
                    value = nick,
                    onValueChange = { nick = it },
                    placeholder = {
                        Text(
                            "Придумайте уникальный ник",
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
                        keyboardType = KeyboardType.Text,
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
                    text = "Дата рождения",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue
                    )
                )
                Spacer(modifier = Modifier.padding(top = 6.dp))
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { newText ->
                        val filtered = newText.filter { it.isDigit() }.take(8)
                        dateOfBirth = filtered
                    },
                    placeholder = {
                        Text(
                            "Укажите в формате ДД.ММ.ГГГГ",
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
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = DateTransformation(),
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
                // Чекбокс согласия
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconToggleButton(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it }
                    ) {
                        Image(
                            painter = painterResource(
                                if (termsAccepted) R.drawable.check2
                                else R.drawable.check1
                            ),
                            contentDescription = if (termsAccepted) "Checked" else "Unchecked",
                            modifier = Modifier.size(24.dp),
                            colorFilter = null // Отключаем стандартный tint
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Устанавливая этот флажок, вы соглашаетесь с ",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(start = 0.dp, top = 5.dp, end = 0.dp, bottom = 0.dp)
                        )
                        TextButton(
                            onClick = onOpenPrivacyPolicy,
                            modifier = Modifier.padding(start = 0.dp, top = 5.dp, end = 0.dp, bottom = 0.dp)
                                .offset(y = (-8).dp) // Компенсация стандартного отступа

                        ) {
                            Text(
                                text = "Положениями и условиями и политикой конфиденциальности",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
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
                    onClick = {
                        onRegisterClick(nick, dateOfBirth, termsAccepted)
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
                            color = White
                        )
                    )
                }
            }
        }

        GlobalToast()

    }
}

private fun readPrivacyPolicyFromAssets(context: Context): String {
    return try {
        val inputStream = context.assets.open("privacy_policy.html")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
        }
        reader.close()
        inputStream.close()
        stringBuilder.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        """
        <html>
            <body>
                <h1>Ошибка загрузки политики конфиденциальности</h1>
                <p>Пожалуйста, свяжитесь с поддержкой приложения</p>
            </body>
        </html>
        """
    }
}

private fun openPrivacyPolicy(context: Context) {
    try {
        val intent = Intent(context, PrivacyPolicyActivity::class.java)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        ToastManager.show("Не удалось открыть политику конфиденциальности", ToastType.ERROR)
    }
}

class PrivacyPolicyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            YourDayTheme {
                PrivacyPolicyScreen()
            }
        }
    }
}

@Composable
fun PrivacyPolicyScreen() {
    val context = LocalContext.current
    val htmlContent = remember { readPrivacyPolicyFromAssets(context) }

    Scaffold(
    ) { paddingValues ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    loadDataWithBaseURL(
                        null,
                        htmlContent,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            when (i) {
                1 -> out += "."
                3 -> out += "."
            }
        }

        // Создаем mapping для курсора
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Преобразуем позицию в исходном тексте в позицию в отформатированном
                return when {
                    offset <= 1 -> offset
                    offset <= 3 -> offset + 1
                    offset <= 7 -> offset + 2
                    else -> 10 // ДД.ММ.ГГГГ - 10 символов
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Преобразуем позицию в отформатированном тексте в позицию в исходном
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

