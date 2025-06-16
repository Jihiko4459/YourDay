package com.example.yourday.repository.tasks

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskType
import kotlinx.coroutines.flow.Flow

class TaskTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalTaskType>> {
        return db.taskTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalTaskType? {
        return db.taskTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalTaskType) {
        db.taskTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalTaskType) {
        db.taskTypeDao().delete(type)
    }
}