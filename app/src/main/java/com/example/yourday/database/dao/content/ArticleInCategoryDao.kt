package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalArticleInCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleInCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articleInCategory: LocalArticleInCategory)

    @Update
    suspend fun update(articleInCategory: LocalArticleInCategory)

    @Delete
    suspend fun delete(articleInCategory: LocalArticleInCategory)

    @Query("SELECT * FROM articles_in_categories WHERE articleId = :articleId")
    fun getByArticle(articleId: Int): Flow<List<LocalArticleInCategory>>

    @Query("SELECT * FROM articles_in_categories WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalArticleInCategory>
}