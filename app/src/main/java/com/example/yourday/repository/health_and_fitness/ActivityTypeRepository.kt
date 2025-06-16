package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalActivityType
import kotlinx.coroutines.flow.Flow

class ActivityTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalActivityType>> {
        return db.activityTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalActivityType? {
        return db.activityTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalActivityType) {
        db.activityTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalActivityType) {
        db.activityTypeDao().delete(type)
    }

    suspend fun deleteAll() {
        db.activityTypeDao().deleteAll()
    }
}