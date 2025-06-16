package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTaskDependency
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDependencyDao {
    @Upsert
    suspend fun upsert(dependency: LocalTaskDependency)

    @Delete
    suspend fun delete(dependency: LocalTaskDependency)

    @Query("SELECT * FROM task_dependencies WHERE id = :id")
    suspend fun getById(id: Int): LocalTaskDependency?

    @Query("SELECT * FROM task_dependencies WHERE taskId = :taskId")
    fun getByTask(taskId: Int): Flow<List<LocalTaskDependency>>

}