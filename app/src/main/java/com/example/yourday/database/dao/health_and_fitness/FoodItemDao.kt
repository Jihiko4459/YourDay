package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalFoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Upsert
    suspend fun upsert(item: LocalFoodItem)

    @Delete
    suspend fun delete(item: LocalFoodItem)

    @Query("SELECT * FROM food_items")
    fun getAll(): Flow<List<LocalFoodItem>>

    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getById(id: Int): LocalFoodItem?
    // Новый метод для очистки таблицы
    @Query("DELETE FROM food_items")
    suspend fun deleteAll()
}