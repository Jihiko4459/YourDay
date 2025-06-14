package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTaskPriorityType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskPriorityTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: LocalTaskPriorityType)

    @Update
    suspend fun update(type: LocalTaskPriorityType)

    @Delete
    suspend fun delete(type: LocalTaskPriorityType)

    @Query("SELECT * FROM task_priority_types")
    fun getAll(): Flow<List<LocalTaskPriorityType>>

    @Query("SELECT * FROM task_priority_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTaskPriorityType>
}