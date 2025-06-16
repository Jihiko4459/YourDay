package com.example.yourday.database.dao.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTask
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsert(task: LocalTask)

    @Delete
    suspend fun delete(task: LocalTask)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Int): LocalTask?

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalTask>>

    @Query("SELECT * FROM tasks WHERE userId = :userId AND date(dueDate) = date(:date)")
    fun getTasksByDate(userId: String, date: String): Flow<List<LocalTask>>
}