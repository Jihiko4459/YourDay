package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalNutritionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionLogDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(log: LocalNutritionLog)

    @Update
    suspend fun update(log: LocalNutritionLog)

    @Delete
    suspend fun delete(log: LocalNutritionLog)

    @Query("SELECT * FROM nutrition_logs WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalNutritionLog>>

    @Query("SELECT * FROM nutrition_logs WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalNutritionLog>
}