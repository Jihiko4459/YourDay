package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalMealType
import kotlinx.coroutines.flow.Flow

@Dao
interface MealTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: LocalMealType)

    @Update
    suspend fun update(type: LocalMealType)

    @Delete
    suspend fun delete(type: LocalMealType)

    @Query("SELECT * FROM meal_types")
    fun getAll(): Flow<List<LocalMealType>>

    @Query("SELECT * FROM meal_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalMealType>
}