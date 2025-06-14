package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticleCategory
import kotlinx.coroutines.flow.Flow

// Article Categories
@Dao
interface ArticleCategoryDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(category: LocalArticleCategory)

    @Upsert
    suspend fun update(category: LocalArticleCategory)

    @Delete
    suspend fun delete(category: LocalArticleCategory)

    @Query("SELECT * FROM article_categories")
    fun getAll(): Flow<List<LocalArticleCategory>>

    @Query("SELECT * FROM article_categories WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalArticleCategory>
}