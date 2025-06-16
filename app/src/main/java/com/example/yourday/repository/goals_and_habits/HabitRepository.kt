package com.example.yourday.repository.goals_and_habits

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalHabit
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val db: YourDayDatabase) {
    fun getByUser(userId: String): Flow<List<LocalHabit>> {
        return db.habitDao().getByUser(userId)
    }

    suspend fun getById(id: Int): LocalHabit? {
        return db.habitDao().getById(id)
    }

    suspend fun upsert(habit: LocalHabit) {
        db.habitDao().upsert(habit)
    }

    suspend fun delete(habit: LocalHabit) {
        db.habitDao().delete(habit)
    }
}
