package com.example.yourday.repository.goals_and_habits

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGoalAndHabitType
import kotlinx.coroutines.flow.Flow

class GoalAndHabitTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalGoalAndHabitType>> {
        return db.goalAndHabitTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalGoalAndHabitType? {
        return db.goalAndHabitTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalGoalAndHabitType) {
        db.goalAndHabitTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalGoalAndHabitType) {
        db.goalAndHabitTypeDao().delete(type)
    }

    // Добавляем все справочные типы
    suspend fun insertAllReferenceTypes(types: List<LocalGoalAndHabitType>) {
        db.goalAndHabitTypeDao().insertAll(types)
    }

    // Удаляем все справочные типы
    suspend fun deleteAllReferenceTypes() {
        db.goalAndHabitTypeDao().deleteAllReferenceTypes()
    }
}