package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Upsert
    suspend fun upsert(goal: LocalGoal)

    @Delete
    suspend fun delete(goal: LocalGoal)

    @Query("SELECT * FROM goals WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalGoal>>

    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getById(id: Int): LocalGoal?
}