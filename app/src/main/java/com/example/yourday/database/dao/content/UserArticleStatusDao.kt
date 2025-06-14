package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalUserArticleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UserArticleStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: LocalUserArticleStatus)

    @Update
    suspend fun update(status: LocalUserArticleStatus)

    @Delete
    suspend fun delete(status: LocalUserArticleStatus)

    @Query("SELECT * FROM user_article_statuses WHERE userId = :userId AND articleId = :articleId")
    fun getByArticle(userId: String, articleId: Int): Flow<LocalUserArticleStatus?>

    @Query("SELECT * FROM user_article_statuses WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalUserArticleStatus>
}