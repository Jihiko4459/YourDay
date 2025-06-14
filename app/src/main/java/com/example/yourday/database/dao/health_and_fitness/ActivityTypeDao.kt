package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalActivityType
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityTypeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(type: LocalActivityType)

    @Upsert
    suspend fun update(type: LocalActivityType)

    @Delete
    suspend fun delete(type: LocalActivityType)

    @Query("SELECT * FROM activity_types")
    fun getAll(): Flow<List<LocalActivityType>>

    @Query("SELECT * FROM activity_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalActivityType>
}