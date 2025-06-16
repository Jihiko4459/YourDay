package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTaskType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskTypeDao {
    @Upsert
    suspend fun upsert(type: LocalTaskType)

    @Delete
    suspend fun delete(type: LocalTaskType)

    @Query("SELECT * FROM task_types")
    fun getAll(): Flow<List<LocalTaskType>>

    @Query("SELECT * FROM task_types WHERE id = :id")
    suspend fun getById(id: Int): LocalTaskType?
}