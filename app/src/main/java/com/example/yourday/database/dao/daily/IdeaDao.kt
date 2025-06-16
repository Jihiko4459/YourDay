package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalIdea
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Upsert
    suspend fun upsert(idea: LocalIdea)

    @Delete
    suspend fun delete(idea: LocalIdea)

    @Query("SELECT * FROM ideas WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalIdea>>

    @Query("SELECT * FROM ideas WHERE id = :id")
    suspend fun getById(id: Int): LocalIdea?
}