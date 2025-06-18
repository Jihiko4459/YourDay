package com.example.yourday.data

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Task
import io.github.jan.supabase.exceptions.UnknownRestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TaskRepository {
    private val mockTasks = mutableListOf<Task>()
    private var nextMockId = 1

    suspend fun getTaskById(taskId: Int, userId: String, supabaseHelper: SupabaseHelper): Task? {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getTaskById(taskId, userId)
            } catch (e: Exception) {
                mockTasks.firstOrNull { it.id == taskId && it.userId == userId }
            }
        }
    }

    suspend fun saveTask(task: Task, supabaseHelper: SupabaseHelper): Task {
        return withContext(Dispatchers.IO) {
            try {
                // For new tasks (id=0), we need to handle the sequence permission error
                if (task.id == 0) {
                    try {
                        // First try to save with Supabase
                        val savedTask = supabaseHelper.saveTask(task)
                        mockTasks.removeIf { it.id == savedTask.id }
                        savedTask
                    } catch (e: UnknownRestException) {
                        if (e.message?.contains("permission denied for sequence tasks_id_seq") == true) {
                            // Fallback to mock with manual ID assignment
                            val newTask = task.copy(id = nextMockId++)
                            mockTasks.removeIf { it.id == newTask.id }
                            mockTasks.add(newTask)
                            newTask
                        } else {
                            throw e
                        }
                    }
                } else {
                    // For existing tasks, try to update in Supabase
                    try {
                        val savedTask = supabaseHelper.saveTask(task)
                        mockTasks.removeIf { it.id == task.id }
                        savedTask
                    } catch (e: Exception) {
                        // Fallback to mock
                        mockTasks.removeIf { it.id == task.id }
                        mockTasks.add(task)
                        task
                    }
                }
            } catch (e: Exception) {
                // General fallback to mock
                val newTask = if (task.id == 0) {
                    task.copy(id = nextMockId++)
                } else {
                    task
                }
                mockTasks.removeIf { it.id == newTask.id }
                mockTasks.add(newTask)
                newTask
            }
        }
    }

    suspend fun deleteTask(taskId: Int, userId: String, supabaseHelper: SupabaseHelper): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.deleteTask(taskId)
                mockTasks.removeIf { it.id == taskId }
                true
            } catch (e: Exception) {
                mockTasks.removeIf { it.id == taskId && it.userId == userId }
                true
            }
        }
    }

    suspend fun getDailyTasks(date: String, userId: String, supabaseHelper: SupabaseHelper): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getDailyTasks(date, userId)
            } catch (e: Exception) {
                mockTasks.filter { it.dueDate == date && it.userId == userId }
            }
        }
    }
}