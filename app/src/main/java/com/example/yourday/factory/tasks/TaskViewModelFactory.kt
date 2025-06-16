package com.example.yourday.factory.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.tasks.TaskRepository
import com.example.yourday.viewmodel.tasks.TaskViewModel

class TaskViewModelFactory(
    val app: Application,
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(app, repository) as T
    }
}
