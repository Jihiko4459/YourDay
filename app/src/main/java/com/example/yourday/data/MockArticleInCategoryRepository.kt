package com.example.yourday.data

import com.example.yourday.model.ArticleInCategory

/**
 * Объект-репозиторий для работы с мок-данными связей между статьями и категориями.
 * Используется для тестирования и как fallback при отсутствии данных из сети.
 */
object MockArticleInCategoryRepository {
    private val mockArticleInCategories = listOf(
        ArticleInCategory(1, 1, 1),
        ArticleInCategory(2, 2, 1),
        ArticleInCategory(3, 2, 10),
        ArticleInCategory(4, 2, 5),
        ArticleInCategory(5, 3, 1),
        ArticleInCategory(6, 3, 10),
        ArticleInCategory(7, 4, 2),
        ArticleInCategory(8, 4, 5),
        ArticleInCategory(9, 5, 2),
        ArticleInCategory(10, 5, 10)
    )

    /**
     * Получает все связи для конкретной статьи.
     *
     * @param articleId ID статьи для поиска связей
     * @return Список связей ArticleInCategory для указанной статьи
     */
    fun getRelationsForArticle(articleId: Int): List<ArticleInCategory> {
        return mockArticleInCategories.filter { it.articleId == articleId }
    }

    /**
     * Получает все связи для конкретной категории.
     *
     * @param categoryId ID категории для поиска связей
     * @return Список связей ArticleInCategory для указанной категории
     */
    fun getRelationsForCategory(categoryId: Int): List<ArticleInCategory> {
        return mockArticleInCategories.filter { it.categoryId == categoryId }
    }

    /**
     * Получает все существующие связи между статьями и категориями.
     *
     * @return Полный список всех связей ArticleInCategory
     */
    fun getAllRelations(): List<ArticleInCategory> {
        return mockArticleInCategories
    }
}