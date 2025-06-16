package com.example.yourday.repository.content

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleCategory
import kotlinx.coroutines.flow.Flow

class ArticleCategoryRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalArticleCategory>> {
        return db.articleCategoryDao().getAll()
    }

    suspend fun getById(id: Int): LocalArticleCategory? {
        return db.articleCategoryDao().getById(id)
    }

    suspend fun upsert(category: LocalArticleCategory) {
        db.articleCategoryDao().upsert(category)
    }

    suspend fun delete(category: LocalArticleCategory) {
        db.articleCategoryDao().delete(category)
    }

    suspend fun deleteAll() {
        db.articleCategoryDao().deleteAll()
    }


}