package com.example.yourday.repository.tasks

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskPriorityType
import kotlinx.coroutines.flow.Flow

class TaskPriorityTypeRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalTaskPriorityType>> {
        return db.taskPriorityTypeDao().getAll()
    }

    suspend fun getById(id: Int): LocalTaskPriorityType? {
        return db.taskPriorityTypeDao().getById(id)
    }

    suspend fun upsert(type: LocalTaskPriorityType) {
        db.taskPriorityTypeDao().upsert(type)
    }

    suspend fun delete(type: LocalTaskPriorityType) {
        db.taskPriorityTypeDao().delete(type)
    }
}