package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalWaterIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {

    @Upsert
    suspend fun upsert(intake: LocalWaterIntake)

    @Delete
    suspend fun delete(intake: LocalWaterIntake)


    @Query("SELECT * FROM water_intake WHERE id = :id")
    suspend fun getById(id: Int): LocalWaterIntake?

    @Query("SELECT * FROM water_intake WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalWaterIntake>>

}