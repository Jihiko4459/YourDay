package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalWaterIntake
import kotlinx.coroutines.flow.Flow

class WaterIntakeRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalWaterIntake>> {
        return db.waterIntakeDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalWaterIntake? {
        return db.waterIntakeDao().getById(id)
    }

    suspend fun upsert(intake: LocalWaterIntake) {
        db.waterIntakeDao().upsert(intake)
    }


    suspend fun delete(intake: LocalWaterIntake) {
        db.waterIntakeDao().delete(intake)
    }
}