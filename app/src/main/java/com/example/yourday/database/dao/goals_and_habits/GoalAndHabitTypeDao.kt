package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalGoalAndHabitType
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalAndHabitTypeDao {
    @Upsert
    suspend fun upsert(type: LocalGoalAndHabitType)

    @Delete
    suspend fun delete(type: LocalGoalAndHabitType)

    @Query("SELECT * FROM goal_and_habit_types")
    fun getAll(): Flow<List<LocalGoalAndHabitType>>

    @Query("SELECT * FROM goal_and_habit_types WHERE id = :id")
    suspend fun getById(id: Int): LocalGoalAndHabitType?

    // Вставка списка типов
    @Upsert
    suspend fun insertAll(types: List<LocalGoalAndHabitType>)

    // Удаление всех справочных типов
    @Query("DELETE FROM goal_and_habit_types")
    suspend fun deleteAllReferenceTypes()
}