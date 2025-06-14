package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalSteps
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(steps: LocalSteps)

    @Update
    suspend fun update(steps: LocalSteps)

    @Delete
    suspend fun delete(steps: LocalSteps)

    @Query("SELECT * FROM steps WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalSteps?>

    @Query("SELECT * FROM steps WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalSteps>
}
