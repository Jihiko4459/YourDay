package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalMedication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Upsert
    suspend fun upsert(medication: LocalMedication)

    @Delete
    suspend fun delete(medication: LocalMedication)

    @Query("SELECT * FROM medications WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalMedication>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getById(id: Int): LocalMedication?
}