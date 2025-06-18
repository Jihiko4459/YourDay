// ArticleData.kt
package com.example.yourday.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.model.ArticleInCategory

object ArticleData {
    var articles: List<Article> by mutableStateOf(emptyList())
    var categories: List<ArticleCategory> by mutableStateOf(emptyList())
    var articleInCategories: List<ArticleInCategory> by mutableStateOf(emptyList())

    // Get articles filtered by category
    fun getArticlesByCategory(categoryId: Int?): List<Article> {
        return if (categoryId == null) {
            articles
        } else {
            val articleIds = articleInCategories
                .filter { it.categoryId == categoryId }
                .map { it.articleId }
            articles.filter { it.id in articleIds }
        }
    }

    // Get categories for specific article
    fun getCategoriesForArticle(articleId: Int): List<ArticleCategory> {
        return articleInCategories
            .filter { it.articleId == articleId }
            .mapNotNull { aic ->
                categories.firstOrNull { it.id == aic.categoryId }
            }
    }

    // Load data with Supabase fallback to mocks
    suspend fun loadData(supabaseHelper: SupabaseHelper): Boolean {
        return try {
            articles = supabaseHelper.getArticles().ifEmpty { MockArticleRepository.getAllArticles() }
            categories = supabaseHelper.getArticleCategories().ifEmpty { MockCategoryRepository.getAllCategories() }
            articleInCategories = supabaseHelper.getArticleInCategories().ifEmpty {
                MockArticleInCategoryRepository.getAllRelations()
            }
            true
        } catch (e: Exception) {
            articles = MockArticleRepository.getAllArticles()
            categories = MockCategoryRepository.getAllCategories()
            articleInCategories = MockArticleInCategoryRepository.getAllRelations()
            false
        }
    }
}