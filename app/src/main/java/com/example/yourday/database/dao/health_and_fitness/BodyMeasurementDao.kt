package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalBodyMeasurement
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyMeasurementDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(measurement: LocalBodyMeasurement)

    @Update
    suspend fun update(measurement: LocalBodyMeasurement)

    @Delete
    suspend fun delete(measurement: LocalBodyMeasurement)

    @Query("SELECT * FROM body_measurements WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalBodyMeasurement?>

    @Query("SELECT * FROM body_measurements WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalBodyMeasurement>
}