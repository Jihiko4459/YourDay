package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalGoalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: LocalGoalStatus)

    @Update
    suspend fun update(status: LocalGoalStatus)

    @Delete
    suspend fun delete(status: LocalGoalStatus)

    @Query("SELECT * FROM goal_statuses")
    fun getAll(): Flow<List<LocalGoalStatus>>

    @Query("SELECT * FROM goal_statuses WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalGoalStatus>
}