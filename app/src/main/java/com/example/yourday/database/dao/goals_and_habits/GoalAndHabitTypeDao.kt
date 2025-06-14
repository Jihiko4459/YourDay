package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalGoalAndHabitType
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalAndHabitTypeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(type: LocalGoalAndHabitType)

    @Update
    suspend fun update(type: LocalGoalAndHabitType)

    @Delete
    suspend fun delete(type: LocalGoalAndHabitType)

    @Query("SELECT * FROM goal_and_habit_types")
    fun getAll(): Flow<List<LocalGoalAndHabitType>>

    @Query("SELECT * FROM goal_and_habit_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalGoalAndHabitType>
}