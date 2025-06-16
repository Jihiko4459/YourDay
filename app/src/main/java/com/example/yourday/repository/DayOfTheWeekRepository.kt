package com.example.yourday.repository

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDayOfTheWeek
import kotlinx.coroutines.flow.Flow

class DayOfTheWeekRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalDayOfTheWeek>> {
        return db.dayOfTheWeekDao().getAll()
    }

    suspend fun getById(id: Int): LocalDayOfTheWeek? {
        return db.dayOfTheWeekDao().getById(id)
    }

    suspend fun upsert(day: LocalDayOfTheWeek) {
        db.dayOfTheWeekDao().upsert(day)
    }

    suspend fun delete(day: LocalDayOfTheWeek) {
        db.dayOfTheWeekDao().delete(day)
    }

    // Новый метод для очистки всех данных
    suspend fun deleteAll() {
        db.dayOfTheWeekDao().deleteAll()
    }
}