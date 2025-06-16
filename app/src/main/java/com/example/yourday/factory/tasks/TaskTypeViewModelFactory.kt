package com.example.yourday.factory.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.tasks.TaskTypeRepository
import com.example.yourday.viewmodel.tasks.TaskTypeViewModel

class TaskTypeViewModelFactory(
    val app: Application,
    private val repository: TaskTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskTypeViewModel(app) as T
    }
}