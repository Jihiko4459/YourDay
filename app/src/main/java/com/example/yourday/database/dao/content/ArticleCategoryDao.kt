package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticleCategory
import kotlinx.coroutines.flow.Flow

// Article Categories
@Dao
interface ArticleCategoryDao {
    @Upsert
    suspend fun upsert(category: LocalArticleCategory)

    @Delete
    suspend fun delete(category: LocalArticleCategory)

    @Query("SELECT * FROM article_categories")
    fun getAll(): Flow<List<LocalArticleCategory>>


    @Query("SELECT * FROM article_categories WHERE id = :id")
    suspend fun getById(id: Int): LocalArticleCategory?

    @Query("DELETE FROM article_categories")
    suspend fun deleteAll()




}