package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalIdea
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(idea: LocalIdea)

    @Update
    suspend fun update(idea: LocalIdea)

    @Delete
    suspend fun delete(idea: LocalIdea)

    @Query("SELECT * FROM ideas WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalIdea>>

    @Query("SELECT * FROM ideas WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalIdea>
}