package com.example.yourday.repository.tasks

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTask
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val db: YourDayDatabase) {
    fun getByUser(userId: String): Flow<List<LocalTask>> {
        return db.taskDao().getByUser(userId)
    }

    fun getTasksByDate(userId: String, date: String): Flow<List<LocalTask>> {
        return db.taskDao().getTasksByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalTask? {
        return db.taskDao().getById(id)
    }

    suspend fun upsert(task: LocalTask) {
        db.taskDao().upsert(task)
    }

    suspend fun delete(task: LocalTask) {
        db.taskDao().delete(task)
    }

}