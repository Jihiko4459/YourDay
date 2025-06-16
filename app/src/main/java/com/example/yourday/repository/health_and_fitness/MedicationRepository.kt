package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalMedication
import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val db: YourDayDatabase) {
    fun getByUser(userId: String): Flow<List<LocalMedication>> {
        return db.medicationDao().getByUser(userId)
    }

    suspend fun getById(id: Int): LocalMedication? {
        return db.medicationDao().getById(id)
    }

    suspend fun upsert(medication: LocalMedication) {
        db.medicationDao().upsert(medication)
    }



    suspend fun delete(medication: LocalMedication) {
        db.medicationDao().delete(medication)
    }
}