package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTask

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(task: LocalTask): Long

    @Upsert
    suspend fun update(task: LocalTask)

    @Delete
    suspend fun delete(task: LocalTask)

    @Query("SELECT * FROM tasks WHERE userId = :userId AND date(dueDate) = date(:date)")
    suspend fun getTasksByDate(userId: String, date: String): List<LocalTask>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND isSynced = 0")
    suspend fun getUnsyncedTasks(userId: String): List<LocalTask>
}