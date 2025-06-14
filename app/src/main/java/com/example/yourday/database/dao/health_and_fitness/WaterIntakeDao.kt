package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalWaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intake: LocalWaterIntake)

    @Update
    suspend fun update(intake: LocalWaterIntake)

    @Delete
    suspend fun delete(intake: LocalWaterIntake)

    @Query("SELECT * FROM water_intake WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalWaterIntake>>

    @Query("SELECT * FROM water_intake WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalWaterIntake>
}