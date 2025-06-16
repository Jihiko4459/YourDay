package com.example.yourday

import YourDayTheme
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.yourday.model.Article
import com.example.yourday.screens.mockArticleInCategories
import com.example.yourday.screens.mockArticles
import com.example.yourday.screens.mockCategories
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

                val articleId = intent.getIntExtra("article_id", -1)
                if (articleId == -1) {
                    Toast.makeText(this, "Invalid article", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    ArticleDetailScreen(mockArticles[articleId-1])
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
fun ArticleDetailScreen(article: Article) {
    val scrollState = rememberScrollState()

    val articleCategories = remember {
        mockArticleInCategories.filter { it.articleId == article.id }
            .map { aic ->
                mockCategories.firstOrNull { it.id == aic.categoryId }?.categoryName ?: ""
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Article image
        AsyncImage(
            model = article.articleImage,
            contentDescription = article.title,
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

            // Display all categories for the article
            Column {
                articleCategories.forEach { category ->
                    Text(
                        text = "#$category",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Article title
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // WebView for HTML content
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadDataWithBaseURL(
                            null,
                            article.content.toString(),
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}