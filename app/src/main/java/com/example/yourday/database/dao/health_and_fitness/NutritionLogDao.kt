package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalNutritionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionLogDao {
    @Upsert
    suspend fun upsert(log: LocalNutritionLog)

    @Delete
    suspend fun delete(log: LocalNutritionLog)

    @Query("SELECT * FROM nutrition_logs WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalNutritionLog>>

    @Query("SELECT * FROM nutrition_logs WHERE id = :id")
    suspend fun getById(id: Int): LocalNutritionLog?
}