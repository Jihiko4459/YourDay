package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalBodyMeasurement
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMeasurementDao {
    @Upsert
    suspend fun upsert(measurement: LocalBodyMeasurement)

    @Delete
    suspend fun delete(measurement: LocalBodyMeasurement)

    @Query("SELECT * FROM body_measurements WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalBodyMeasurement?>

    @Query("SELECT * FROM body_measurements WHERE id = :id")
    suspend fun getById(id: Int): LocalBodyMeasurement?

}