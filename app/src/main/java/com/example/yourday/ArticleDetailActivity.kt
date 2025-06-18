package com.example.yourday

import YourDayTheme
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.data.ArticleData
import com.example.yourday.data.MockArticleRepository
import com.example.yourday.model.Article
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class ArticleDetailActivity : ComponentActivity() {
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

                val articleId = intent.getIntExtra("articleId", -1)
                ArticleDetailScreen(articleId)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

@Composable
fun ArticleDetailScreen(articleId: Int) {
    val context = LocalContext.current
    val supabaseHelper= remember { SupabaseHelper(context.applicationContext) }
    var article by remember { mutableStateOf<Article?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Get categories from global ArticleData
    val articleCategories = remember {
        ArticleData.getCategoriesForArticle(articleId).map { it.categoryName }
    }

    LaunchedEffect(articleId) {
        try {
            // First try to find article in global ArticleData
            article = ArticleData.articles.firstOrNull { it.id == articleId }

            // If not found in global data, fetch from Supabase with fallback to mock
            if (article == null) {
                article = try {
                    supabaseHelper.getArticleById(articleId) ?:
                    MockArticleRepository.getArticleById(articleId)
                } catch (e: Exception) {
                    MockArticleRepository.getArticleById(articleId)
                }

                // Add to global data if successfully fetched
                article?.let {
                    ArticleData.articles = ArticleData.articles + it
                }
            }

            isError = false
        } catch (e: Exception) {
            isError = true
            errorMessage = "Ошибка загрузки статьи. Используются локальные данные"
            article = MockArticleRepository.getArticleById(articleId)
        } finally {
            isLoading = false
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding()
            )
    ) {
        if (isLoading) {
            // Show loading indicator
        } else if (article == null) {
            // Show error state
            Text(
                text = errorMessage ?: "Статья не найдена",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // Show article content
            AsyncImage(
                model = article?.articleImage,
                contentDescription = article?.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "7 мин чтения",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Column {
                    articleCategories.forEach { category ->
                        Text(
                            text = "#$category",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = article?.title.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                val content = when {
                    article?.content == null -> "<p>Контент отсутствует</p>"
                    else -> article!!.content
                }

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            loadDataWithBaseURL(
                                null,
                                content.toString(),
                                "text/html",
                                "UTF-8",
                                null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}