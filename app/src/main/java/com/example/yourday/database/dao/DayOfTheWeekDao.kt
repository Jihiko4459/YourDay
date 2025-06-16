package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalDayOfTheWeek
import kotlinx.coroutines.flow.Flow

@Dao
interface DayOfTheWeekDao {
    @Upsert
    suspend fun upsert(day: LocalDayOfTheWeek)

    @Delete
    suspend fun delete(day: LocalDayOfTheWeek)

    @Query("SELECT * FROM days_of_the_week WHERE id = :id")
    suspend fun getById(id: Int): LocalDayOfTheWeek?

    @Query("SELECT * FROM days_of_the_week")
    fun getAll(): Flow<List<LocalDayOfTheWeek>>

    // Новый метод для очистки таблицы
    @Query("DELETE FROM days_of_the_week")
    suspend fun deleteAll()

}