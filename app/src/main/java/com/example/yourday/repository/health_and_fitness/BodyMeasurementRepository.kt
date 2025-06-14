package com.example.yourday.repository.health_and_fitness

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.BodyMeasurement
import com.example.yourday.model.LocalBodyMeasurement
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class BodyMeasurementRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getByDate(userId: String, date: String): Flow<LocalBodyMeasurement?> {
        return db.bodyMeasurementDao().getByDate(userId, date)
    }

    suspend fun insert(measurement: LocalBodyMeasurement) {
        db.bodyMeasurementDao().insert(measurement)
        syncMeasurements()
    }

    private suspend fun syncMeasurements() {
        val unsynced = db.bodyMeasurementDao().getUnsynced()
        unsynced.forEach { measurement ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("body_measurements").upsert(
                        BodyMeasurement(
                            id = measurement.serverId ?: 0,
                            userId = measurement.userId,
                            date = measurement.date,
                            weight = measurement.weight,
                            height = measurement.height,
                            bmi = measurement.bmi,
                            chestCircum = measurement.chestCircum,
                            waistCircum = measurement.waistCircum,
                            hipCircum = measurement.hipCircum
                        )
                    )
                }

                if (result.isSuccess) {
                    db.bodyMeasurementDao().update(measurement.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}