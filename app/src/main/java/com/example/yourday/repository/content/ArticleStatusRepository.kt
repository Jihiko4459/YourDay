package com.example.yourday.repository.content

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleStatus
import kotlinx.coroutines.flow.Flow

class ArticleStatusRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalArticleStatus>> {
        return db.articleStatusDao().getAll()
    }

    suspend fun getById(id: Int): LocalArticleStatus? {
        return db.articleStatusDao().getById(id)
    }

    suspend fun upsert(status: LocalArticleStatus) {
        db.articleStatusDao().upsert(status)
    }


    suspend fun delete(status: LocalArticleStatus) {
        db.articleStatusDao().delete(status)
    }

    suspend fun deleteAll() = db.articleStatusDao().deleteAll()

}