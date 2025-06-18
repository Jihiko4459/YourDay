package com.example.yourday.data

import com.example.yourday.model.Task

object MockTaskRepository {
    private val mockTasks = mutableListOf<Task>()
    private var nextId = 1

    fun getAllTasks(): List<Task> = mockTasks.toList()

    fun getTasksForDate(date: String, userId: String): List<Task> {
        return mockTasks.filter { it.dueDate == date && it.userId == userId }
    }

    fun getTaskById(taskId: Int): Task? {
        return mockTasks.firstOrNull { it.id == taskId }
    }

    fun addTask(task: Task): Task {
        val newTask = task.copy(id = nextId++)
        mockTasks.add(newTask)
        return newTask
    }

    fun updateTask(updatedTask: Task): Task? {
        val index = mockTasks.indexOfFirst { it.id == updatedTask.id }
        return if (index != -1) {
            mockTasks[index] = updatedTask
            updatedTask
        } else {
            null
        }
    }

    fun deleteTask(taskId: Int): Boolean {
        return mockTasks.removeIf { it.id == taskId }
    }
}