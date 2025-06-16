package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalMealType
import kotlinx.coroutines.flow.Flow

@Dao
interface MealTypeDao {
    @Upsert
    suspend fun upsert(type: LocalMealType)

    @Delete
    suspend fun delete(type: LocalMealType)

    @Query("SELECT * FROM meal_types")
    fun getAll(): Flow<List<LocalMealType>>

    @Query("SELECT * FROM meal_types WHERE id = :id")
    suspend fun getById(id: Int): LocalMealType?

    // Добавление всех типов
    @Upsert
    suspend fun insertAll(types: List<LocalMealType>)

    // Удаление всех типов
    @Query("DELETE FROM meal_types")
    suspend fun deleteAllReferenceTypes()

}