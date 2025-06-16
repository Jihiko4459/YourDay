package com.example.yourday.factory.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.tasks.TaskDependencyRepository
import com.example.yourday.viewmodel.tasks.TaskDependencyViewModel

class TaskDependencyViewModelFactory(
    val app: Application,
    private val repository: TaskDependencyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskDependencyViewModel(app) as T
    }
}