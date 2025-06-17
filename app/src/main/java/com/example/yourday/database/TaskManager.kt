package com.example.yourday.database

import androidx.compose.runtime.mutableStateOf
import com.example.yourday.mockTasks
import com.example.yourday.model.Task

object TaskManager {
    private val _tasks = mutableStateOf(mockTasks)
    val tasks: List<Task> get() = _tasks.value

    fun addTask(task: Task) {
        mockTasks.add(task)
        _tasks.value = mockTasks
    }

    fun updateTask(updatedTask: Task) {
        val index = mockTasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            mockTasks[index] = updatedTask
            _tasks.value = mockTasks
        }
    }

    fun deleteTask(taskId: Int) {
        mockTasks.removeAll { it.id == taskId }
        _tasks.value = mockTasks
    }

    fun getTasksForDate(date: String): List<Task> {
        return mockTasks.filter { it.dueDate == date }
    }
}