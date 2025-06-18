package com.example.yourday.data

import com.example.yourday.model.ArticleInCategory

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

    fun getRelationsForArticle(articleId: Int): List<ArticleInCategory> {
        return mockArticleInCategories.filter { it.articleId == articleId }
    }

    fun getRelationsForCategory(categoryId: Int): List<ArticleInCategory> {
        return mockArticleInCategories.filter { it.categoryId == categoryId }
    }

    fun getAllRelations(): List<ArticleInCategory> {
        return mockArticleInCategories
    }
}