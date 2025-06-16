package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticleInCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleInCategoryDao {
    @Upsert
    suspend fun upsert(articleInCategory: LocalArticleInCategory)

    @Delete
    suspend fun delete(articleInCategory: LocalArticleInCategory)

    // 1. Удалить все записи из таблицы
    @Query("DELETE FROM articles_in_categories")
    suspend fun deleteAllArticleInCategoryRelations()

    // 2. Получить все записи из таблицы
    @Query("SELECT * FROM articles_in_categories")
    fun getAllArticleInCategoryRelations(): Flow<List<LocalArticleInCategory>>

    // Отбор по категории
    @Query("SELECT * FROM articles_in_categories WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: Int): Flow<List<LocalArticleInCategory>>

}