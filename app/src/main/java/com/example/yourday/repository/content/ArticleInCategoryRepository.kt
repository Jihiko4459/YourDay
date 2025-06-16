package com.example.yourday.repository.content

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleInCategory
import kotlinx.coroutines.flow.Flow

class ArticleInCategoryRepository(private val db: YourDayDatabase) {
    suspend fun upsert(relation: LocalArticleInCategory) {
        db.articleInCategoryDao().upsert(relation)
    }

    suspend fun delete(relation: LocalArticleInCategory) {
        db.articleInCategoryDao().delete(relation)
    }

    suspend fun deleteAll() {
        db.articleInCategoryDao().deleteAllArticleInCategoryRelations()
    }

    fun getAll(): Flow<List<LocalArticleInCategory>> {
        return db.articleInCategoryDao().getAllArticleInCategoryRelations()
    }

    fun getByCategory(categoryId: Int): Flow<List<LocalArticleInCategory>> =
        db.articleInCategoryDao().getByCategory(categoryId)




}