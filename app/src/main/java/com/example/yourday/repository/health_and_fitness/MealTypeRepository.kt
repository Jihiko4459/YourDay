package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalMealType
import kotlinx.coroutines.flow.Flow

class MealTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalMealType>> {
        return db.mealTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalMealType? {
        return db.mealTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalMealType) {
        db.mealTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalMealType) {
        db.mealTypeDao().delete(type)
    }

    // Добавление справочных данных
    suspend fun insertAllReferenceTypes(types: List<LocalMealType>) {
        db.mealTypeDao().insertAll(types)
    }

    // Удаление справочных данных
    suspend fun deleteAllReferenceTypes() {
        db.mealTypeDao().deleteAllReferenceTypes()
    }
}
