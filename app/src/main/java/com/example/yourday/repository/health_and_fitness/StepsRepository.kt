package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalSteps
import kotlinx.coroutines.flow.Flow

class StepsRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<LocalSteps?> {
        return db.stepsDao().getByDate(userId, date)
    }

    suspend fun upsert(steps: LocalSteps) {
        db.stepsDao().upsert(steps)
    }

    suspend fun delete(steps: LocalSteps) {
        db.stepsDao().delete(steps)
    }
}