package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleStatusDao {
    @Upsert
    suspend fun upsert(status: LocalArticleStatus)

    @Delete
    suspend fun delete(status: LocalArticleStatus)

    @Query("SELECT * FROM article_statuses")
    fun getAll(): Flow<List<LocalArticleStatus>>


    @Query("SELECT * FROM article_statuses WHERE id = :id")
    suspend fun getById(id: Int): LocalArticleStatus?

    @Query("DELETE FROM article_statuses")
    suspend fun deleteAll()
}