package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticle
import kotlinx.coroutines.flow.Flow

// Articles
@Dao
interface ArticleDao {
    @Upsert
    suspend fun upsert(article: LocalArticle)

    @Delete
    suspend fun delete(article: LocalArticle)

    @Query("SELECT * FROM articles")
    fun getAll(): Flow<List<LocalArticle>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getById(id: Int): LocalArticle?


    @Query("DELETE FROM articles")
    suspend fun deleteAll()
}