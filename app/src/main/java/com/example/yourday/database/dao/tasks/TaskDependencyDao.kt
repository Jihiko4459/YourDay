package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTaskDependency
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDependencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dependency: LocalTaskDependency)

    @Update
    suspend fun update(dependency: LocalTaskDependency)

    @Delete
    suspend fun delete(dependency: LocalTaskDependency)

    @Query("SELECT * FROM task_dependencies WHERE taskId = :taskId")
    fun getByTask(taskId: Int): Flow<List<LocalTaskDependency>>

    @Query("SELECT * FROM task_dependencies WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTaskDependency>
}