package com.example.yourday.repository.content

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticle
import kotlinx.coroutines.flow.Flow

class ArticleRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalArticle>> {
        return db.articleDao().getAll()
    }

    suspend fun getById(id: Int): LocalArticle? {
        return db.articleDao().getById(id)
    }


    suspend fun upsert(article: LocalArticle) {
        db.articleDao().upsert(article)
    }

    suspend fun delete(article: LocalArticle) {
        db.articleDao().delete(article)
    }

    suspend fun deleteAll() {
        db.articleDao().deleteAll()
    }
}