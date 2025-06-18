package com.example.yourday.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yourday.R
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.components.CategoryChip
import com.example.yourday.components.GlobalToast
import com.example.yourday.components.ToastDuration
import com.example.yourday.components.ToastManager
import com.example.yourday.components.ToastType
import com.example.yourday.data.ArticleData
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(
    navController: NavController,
    onIntentToDetails: (articleId: Int) -> Unit
) {
    val context = LocalContext.current
    val supabaseHelper= remember { SupabaseHelper(context.applicationContext) }
    var selectedCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val success = ArticleData.loadData(supabaseHelper)
        if (!success) {
            ToastManager.show(
                message = "Загрузка данных временно недоступна. Используются локальные данные",
                type = ToastType.INFO,
                duration = ToastDuration.LONG
            )
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Статьи",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            color = Primary
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    CategoryChip(
                        text = "Все категории",
                        isSelected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                    ArticleData.categories.forEach { category ->
                        CategoryChip(
                            text = category.categoryName,
                            isSelected = selectedCategory?.id == category.id,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(ArticleData.getArticlesByCategory(selectedCategory?.id)) { article ->
                        ArticleCard(
                            article = article,
                            categories = ArticleData.getCategoriesForArticle(article.id),
                            onClick = { onIntentToDetails(article.id) }
                        )
                    }
                }
            }
        }
        GlobalToast()
    }
}


@Composable
fun ArticleCard(
    article: Article,
    categories: List<ArticleCategory>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = article.articleImage,
                contentDescription = article.title,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .padding(6.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(R.drawable.image),
                error = painterResource(R.drawable.image)
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = article.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom=2.dp)
                )
                Text(
                    text = "7 мин чтения",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        color = DarkBlue
                    )
                )

                Column(modifier = Modifier.padding(top=6.dp)) {
                    categories.forEach { category ->
                        Text(
                            text = "#${category.categoryName}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_light)),
                                color = DarkBlue
                            ),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}


