package com.example.yourday.database.dao.content

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalArticleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: LocalArticleStatus)

    @Upsert
    suspend fun update(status: LocalArticleStatus)

    @Delete
    suspend fun delete(status: LocalArticleStatus)

    @Query("SELECT * FROM article_statuses")
    fun getAll(): Flow<List<LocalArticleStatus>>

    @Query("SELECT * FROM article_statuses WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalArticleStatus>
}