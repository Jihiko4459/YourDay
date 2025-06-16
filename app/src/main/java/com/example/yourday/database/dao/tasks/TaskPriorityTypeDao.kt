package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTaskPriorityType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskPriorityTypeDao {
    @Upsert
    suspend fun upsert(type: LocalTaskPriorityType)

    @Delete
    suspend fun delete(type: LocalTaskPriorityType)

    @Query("SELECT * FROM task_priority_types WHERE id = :id")
    suspend fun getById(id: Int): LocalTaskPriorityType?

    @Query("SELECT * FROM task_priority_types")
    fun getAll(): Flow<List<LocalTaskPriorityType>>

}