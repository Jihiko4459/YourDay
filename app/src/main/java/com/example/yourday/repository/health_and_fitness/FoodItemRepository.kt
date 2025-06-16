package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalFoodItem
import kotlinx.coroutines.flow.Flow

class FoodItemRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalFoodItem>> {
        return db.foodItemDao().getAll()
    }

    suspend fun getById(id: Int): LocalFoodItem? {
        return db.foodItemDao().getById(id)
    }

    suspend fun upsert(item: LocalFoodItem) {
        db.foodItemDao().upsert(item)
    }
    suspend fun delete(item: LocalFoodItem) {
        db.foodItemDao().delete(item)
    }

    // Новый метод для очистки всех данных
    suspend fun deleteAll() {
        db.foodItemDao().deleteAll()
    }
}