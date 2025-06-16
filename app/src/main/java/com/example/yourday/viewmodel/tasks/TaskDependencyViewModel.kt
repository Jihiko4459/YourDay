package com.example.yourday.viewmodel.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskDependency
import com.example.yourday.repository.tasks.TaskDependencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskDependencyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskDependencyRepository
    val dependencies: MutableStateFlow<List<LocalTaskDependency>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = TaskDependencyRepository(db)
    }

    fun loadDependenciesForTask(taskId: Int) {
        viewModelScope.launch {
            repository.getByTask(taskId).collect { deps ->
                dependencies.value = deps
            }
        }
    }

    fun upsertDependency(dependency: LocalTaskDependency) {
        viewModelScope.launch {
            repository.upsert(dependency)
        }
    }

    fun deleteDependency(dependency: LocalTaskDependency) {
        viewModelScope.launch {
            repository.delete(dependency)
        }
    }
}