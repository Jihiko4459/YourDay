package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalMedication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: LocalMedication)

    @Update
    suspend fun update(medication: LocalMedication)

    @Delete
    suspend fun delete(medication: LocalMedication)

    @Query("SELECT * FROM medications WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalMedication>>

    @Query("SELECT * FROM medications WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalMedication>
}