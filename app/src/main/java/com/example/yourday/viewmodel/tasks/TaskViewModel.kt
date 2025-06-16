package com.example.yourday.viewmodel.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalTask
import com.example.yourday.repository.tasks.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _tasks = MutableStateFlow<List<LocalTask>>(emptyList())
    val tasks: StateFlow<List<LocalTask>> = _tasks.asStateFlow()

    private val _currentTask = MutableStateFlow<LocalTask?>(null)
    val currentTask: StateFlow<LocalTask?> = _currentTask.asStateFlow()

    private val _tasksByDate = MutableStateFlow<List<LocalTask>>(emptyList())
    val tasksByDate: StateFlow<List<LocalTask>> = _tasksByDate.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            repository.getByUser(userId).collect { tasksList ->
                _tasks.value = tasksList
            }
        }
    }

    fun loadTaskById(id: String) {
        viewModelScope.launch {
            val idInt = id.toIntOrNull() ?: return@launch
            _currentTask.value = repository.getById(idInt)
        }
    }

    fun loadTasksByDate(date: String) {
        viewModelScope.launch {
            repository.getTasksByDate(userId, date).collect { tasksList ->
                _tasksByDate.value = tasksList
            }
        }
    }

    fun upsertTask(task: LocalTask) {
        viewModelScope.launch {
            repository.upsert(task)
        }
    }

    fun deleteTask(task: LocalTask) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}