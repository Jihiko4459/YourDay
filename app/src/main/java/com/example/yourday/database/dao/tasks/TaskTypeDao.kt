package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTaskType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: LocalTaskType)

    @Update
    suspend fun update(type: LocalTaskType)

    @Delete
    suspend fun delete(type: LocalTaskType)

    @Query("SELECT * FROM task_types")
    fun getAll(): Flow<List<LocalTaskType>>

    @Query("SELECT * FROM task_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTaskType>
}