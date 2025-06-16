package com.example.yourday.repository.goals_and_habits

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGoal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val db: YourDayDatabase) {
    fun getByUser(userId: String): Flow<List<LocalGoal>> {
        return db.goalDao().getByUser(userId)
    }

    suspend fun getById(id: Int): LocalGoal? {
        return db.goalDao().getById(id)
    }

    suspend fun upsert(goal: LocalGoal) {
        db.goalDao().upsert(goal)
    }

    suspend fun delete(goal: LocalGoal) {
        db.goalDao().delete(goal)
    }
}
