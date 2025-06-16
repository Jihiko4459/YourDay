package com.example.yourday.factory.tasks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.tasks.TaskPriorityTypeRepository
import com.example.yourday.viewmodel.tasks.TaskPriorityTypeViewModel

class TaskPriorityTypeViewModelFactory(
    val app: Application,
    private val repository: TaskPriorityTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskPriorityTypeViewModel(app) as T
    }
}
