package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalGoalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalStatusDao {
    @Upsert
    suspend fun upsert(status: LocalGoalStatus)

    @Delete
    suspend fun delete(status: LocalGoalStatus)

    @Query("SELECT * FROM goal_statuses")
    fun getAll(): Flow<List<LocalGoalStatus>>

    @Query("SELECT * FROM goal_statuses WHERE id = :id")
    suspend fun getById(id: Int): LocalGoalStatus?

    // Вставка списка статусов
    @Upsert
    suspend fun insertAll(statuses: List<LocalGoalStatus>)

    // Удаление всех справочных статусов
    @Query("DELETE FROM goal_statuses")
    suspend fun deleteAllReferenceStatuses()

}