package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticle
import kotlinx.coroutines.flow.Flow

// Articles
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: LocalArticle)

    @Upsert
    suspend fun update(article: LocalArticle)

    @Delete
    suspend fun delete(article: LocalArticle)

    @Query("SELECT * FROM articles")
    fun getAll(): Flow<List<LocalArticle>>

    @Query("SELECT * FROM articles WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalArticle>
}