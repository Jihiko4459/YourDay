package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalFoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: LocalFoodItem)

    @Update
    suspend fun update(item: LocalFoodItem)

    @Delete
    suspend fun delete(item: LocalFoodItem)

    @Query("SELECT * FROM food_items")
    fun getAll(): Flow<List<LocalFoodItem>>

    @Query("SELECT * FROM food_items WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalFoodItem>
}