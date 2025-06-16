package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.ActivityTypeRepository
import com.example.yourday.viewmodel.health_and_fitness.ActivityTypeViewModel

class ActivityTypeViewModelFactory(
    val app: Application,
    private val repository: ActivityTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActivityTypeViewModel(app) as T
    }
}