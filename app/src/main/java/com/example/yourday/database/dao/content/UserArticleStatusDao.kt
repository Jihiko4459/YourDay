package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalUserArticleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserArticleStatusDao {
    @Upsert
    suspend fun upsert(status: LocalUserArticleStatus)

    @Delete
    suspend fun delete(status: LocalUserArticleStatus)

    @Query("SELECT * FROM user_article_statuses WHERE userId = :userId AND articleId = :articleId")
    fun getByArticle(userId: String, articleId: Int): Flow<LocalUserArticleStatus?>

    @Query("SELECT * FROM user_article_statuses WHERE id = :id")
    suspend fun getById(id: Int): LocalUserArticleStatus?
}