package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalSteps
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalSteps?> // Change to Flow

    @Upsert
    suspend fun upsert(steps: LocalSteps)

    @Delete
    suspend fun delete(steps: LocalSteps)
}
