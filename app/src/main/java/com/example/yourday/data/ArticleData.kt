package com.example.yourday.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.model.ArticleInCategory


/**
 * Объект для хранения и управления данными статей.
 * Использует Compose State для реактивного обновления UI при изменении данных.
 */
object ArticleData {
    // Список всех статей с возможностью наблюдения за изменениями
    var articles: List<Article> by mutableStateOf(emptyList())
    // Список всех категорий статей с возможностью наблюдения за изменениями
    var categories: List<ArticleCategory> by mutableStateOf(emptyList())
    // Связи между статьями и категориями с возможностью наблюдения за изменениями
    var articleInCategories: List<ArticleInCategory> by mutableStateOf(emptyList())

    /**
     * Получает статьи, отфильтрованные по категории.
     *
     * @param categoryId ID категории для фильтрации (null - все статьи)
     * @return Список статей в указанной категории или все статьи, если categoryId = null
     */
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

    /**
     * Получает категории для конкретной статьи.
     *
     * @param articleId ID статьи
     * @return Список категорий, к которым принадлежит статья
     */
    fun getCategoriesForArticle(articleId: Int): List<ArticleCategory> {
        return articleInCategories
            .filter { it.articleId == articleId }
            .mapNotNull { aic ->
                categories.firstOrNull { it.id == aic.categoryId }
            }
    }


    /**
     * Загружает данные статей из Supabase или использует мок-данные в случае ошибки.
     *
     * @param supabaseHelper Клиент для работы с Supabase
     * @return true, если данные успешно загружены из Supabase, false - если использованы моки
     */
    suspend fun loadData(supabaseHelper: SupabaseHelper): Boolean {
        return try {
            // Пытаемся загрузить данные из Supabase
            val supabaseArticles = supabaseHelper.getArticles()
            val supabaseCategories = supabaseHelper.getArticleCategories()
            val supabaseRelations = supabaseHelper.getArticleInCategories()

            // Обновляем состояние независимо от того, пустые данные или нет
            articles = supabaseArticles.ifEmpty { MockArticleRepository.getAllArticles() }
            categories = supabaseCategories.ifEmpty { MockCategoryRepository.getAllCategories() }
            articleInCategories = supabaseRelations.ifEmpty { MockArticleInCategoryRepository.getAllRelations() }

            true
        } catch (e: Exception) {
            // В случае ошибки используем мок-данные
            articles = MockArticleRepository.getAllArticles()
            categories = MockCategoryRepository.getAllCategories()
            articleInCategories = MockArticleInCategoryRepository.getAllRelations()
            false
        }
    }
}