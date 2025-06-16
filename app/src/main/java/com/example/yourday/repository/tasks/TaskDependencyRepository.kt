package com.example.yourday.repository.tasks

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskDependency
import kotlinx.coroutines.flow.Flow

class TaskDependencyRepository(
    private val db: YourDayDatabase) {
    fun getByTask(taskId: Int): Flow<List<LocalTaskDependency>> {
        return db.taskDependencyDao().getByTask(taskId)
    }

    suspend fun getById(id: Int): LocalTaskDependency? {
        return db.taskDependencyDao().getById(id)
    }

    suspend fun upsert(dependency: LocalTaskDependency) {
        db.taskDependencyDao().upsert(dependency)
    }

    suspend fun delete(dependency: LocalTaskDependency) {
        db.taskDependencyDao().delete(dependency)
    }
}