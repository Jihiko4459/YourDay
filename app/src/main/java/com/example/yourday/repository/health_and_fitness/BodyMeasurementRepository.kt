package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalBodyMeasurement
import kotlinx.coroutines.flow.Flow

class BodyMeasurementRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<LocalBodyMeasurement?> {
        return db.bodyMeasurementDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalBodyMeasurement? {
        return db.bodyMeasurementDao().getById(id)
    }

    suspend fun upsert(measurement: LocalBodyMeasurement) {
        db.bodyMeasurementDao().upsert(measurement)
    }

    suspend fun delete(measurement: LocalBodyMeasurement) {
        db.bodyMeasurementDao().delete(measurement)
    }
}