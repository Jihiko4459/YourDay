package com.example.yourday.repository.content

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalUserArticleStatus
import kotlinx.coroutines.flow.Flow

class UserArticleStatusRepository(private val db: YourDayDatabase) {
    fun getByArticle(userId: String, articleId: Int): Flow<LocalUserArticleStatus?> {
        return db.userArticleStatusDao().getByArticle(userId, articleId)
    }

    suspend fun getById(id: Int): LocalUserArticleStatus? {
        return db.userArticleStatusDao().getById(id)
    }

    suspend fun upsert(status: LocalUserArticleStatus) {
        db.userArticleStatusDao().upsert(status)
    }

    suspend fun delete(status: LocalUserArticleStatus) {
        db.userArticleStatusDao().delete(status)
    }
}