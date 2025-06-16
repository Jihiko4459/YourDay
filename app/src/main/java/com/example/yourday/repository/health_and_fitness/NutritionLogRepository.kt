package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalNutritionLog
import kotlinx.coroutines.flow.Flow

class NutritionLogRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalNutritionLog>> {
        return db.nutritionLogDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalNutritionLog? {
        return db.nutritionLogDao().getById(id)
    }

    suspend fun upsert(log: LocalNutritionLog) {
        db.nutritionLogDao().upsert(log)
    }

    suspend fun delete(log: LocalNutritionLog) {
        db.nutritionLogDao().delete(log)
    }
}