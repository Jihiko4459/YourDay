package com.example.yourday.repository.goals_and_habits

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGoalStatus
import kotlinx.coroutines.flow.Flow

class GoalStatusRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalGoalStatus>> {
        return db.goalStatusDao().getAll()
    }

    suspend fun getById(id: Int): LocalGoalStatus? {
        return db.goalStatusDao().getById(id)
    }

    suspend fun upsert(status: LocalGoalStatus) {
        db.goalStatusDao().upsert(status)
    }
    suspend fun delete(status: LocalGoalStatus) {
        db.goalStatusDao().delete(status)
    }

    // Добавляем все справочные статусы
    suspend fun insertAllReferenceStatuses(statuses: List<LocalGoalStatus>) {
        db.goalStatusDao().insertAll(statuses)
    }

    // Удаляем все справочные статусы
    suspend fun deleteAllReferenceStatuses() {
        db.goalStatusDao().deleteAllReferenceStatuses()
    }
}