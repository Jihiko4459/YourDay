package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: LocalGoal)

    @Update
    suspend fun update(goal: LocalGoal)

    @Delete
    suspend fun delete(goal: LocalGoal)

    @Query("SELECT * FROM goals WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalGoal>>

    @Query("SELECT * FROM goals WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalGoal>
}