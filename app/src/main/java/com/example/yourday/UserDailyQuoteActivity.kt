package com.example.yourday

import YourDayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.DailyPhoto
import com.example.yourday.model.DailyQuote
import com.example.yourday.database.DailyQuoteManager
import com.example.yourday.database.MotivationalCardManager
import io.github.jan.supabase.postgrest.postgrest

class UserDailyQuoteActivity : ComponentActivity() {

    private val supabaseHelper by lazy { SupabaseHelper() }
    private lateinit var quoteManager: DailyQuoteManager
    private lateinit var cardManager: MotivationalCardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteManager = DailyQuoteManager(this)
        cardManager = MotivationalCardManager(this)

        enableEdgeToEdge()
        setContent {
            YourDayTheme {
                var quote by remember { mutableStateOf<DailyQuote?>(null) }
                var photoUrl by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var retryTrigger by remember { mutableStateOf(0) }


                //Основной эффект загрузки
                LaunchedEffect(retryTrigger) {
                    if (retryTrigger >= 0) { // Запускаем при первом рендере и при каждом retry
                        loadDailyQuote(
                            cardManager = cardManager,
                            helper = supabaseHelper,
                            onSuccess = { q, p ->
                                quote = q
                                photoUrl = p
                                isLoading = false
                            },
                            onError = { error ->
                                errorMessage = error
                                isLoading = false
                            }
                        )
                    }
                }


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        errorMessage != null -> {
                            ErrorScreen(
                                message = errorMessage!!,
                                onRetry = {
                                    errorMessage = null
                                    isLoading = true
                                    retryTrigger++ // Увеличиваем триггер для повторного запуска LaunchedEffect
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        else -> {
                            DailyQuoteScreen(
                                quote = quote,
                                photoUrl = photoUrl,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadDailyQuote(
        cardManager: MotivationalCardManager,
        helper: SupabaseHelper,
        onSuccess: (DailyQuote?, String?) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Проверяем аутентификацию
            if (!helper.ensureAuthenticated()) {
                throw Exception("User not authenticated. Please sign in.")
            }

            // Проверяем, есть ли сохраненная карточка на сегодня
            val savedCardId = cardManager.getTodaysCardIndex()

            if (savedCardId != null) {
                // Загружаем сохраненную карточку
                val card = helper.getMotivationalCardById(savedCardId)

                // Загружаем связанные данные
                val quote = card.quoteId?.let { quoteId ->
                    helper.client.postgrest.from("daily_quotes")
                        .select {
                            filter { eq("id", quoteId) }
                        }
                        .decodeSingleOrNull<DailyQuote>()
                }

                val photo = card.photoId?.let { photoId ->
                    helper.client.postgrest.from("daily_photos")
                        .select {
                            filter { eq("id", photoId) }
                        }
                        .decodeSingleOrNull<DailyPhoto>()
                }

                onSuccess(quote, photo?.photoUrl)
            } else {
                // Получаем новую случайную карточку
                val card = helper.getRandomMotivationalCard()
                // Сохраняем индекс карточки
                cardManager.saveTodaysCardIndex(card.id)

                // Загружаем связанные данные
                val quote = card.quoteId?.let { quoteId ->
                    helper.client.postgrest.from("daily_quotes")
                        .select {
                            filter { eq("id", quoteId) }
                        }
                        .decodeSingleOrNull<DailyQuote>()
                }

                val photo = card.photoId?.let { photoId ->
                    helper.client.postgrest.from("daily_photos")
                        .select {
                            filter { eq("id", photoId) }
                        }
                        .decodeSingleOrNull<DailyPhoto>()
                }

                onSuccess(quote, photo?.photoUrl)
            }
        } catch (e: Exception) {
            Logger.e(e) { "Error loading daily quote" }
            onError("Error loading daily quote: ${e.message ?: "Unknown error"}")
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}
@Composable
fun DailyQuoteScreen(
    quote: DailyQuote?,
    photoUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        photoUrl?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .apply {
                            crossfade(true)
                            placeholder(R.drawable.placeholder_quote)
                            error(R.drawable.error_image)
                        }
                        .build()
                ),
                contentDescription = "Daily inspiration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        quote?.let { q ->
            Text(
                text = "\"${q.quote}\"",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "- ${q.authorQuote}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
        } ?: run {
            Text(
                text = "Не удалось загрузить цитату дня",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YourDayTheme {
        DailyQuoteScreen(
            quote = DailyQuote(1, "Test quote", "2023-01-01", "Author"),
            photoUrl = null
        )
    }
}