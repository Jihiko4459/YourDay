package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.UserActivityRepository
import com.example.yourday.viewmodel.health_and_fitness.UserActivityViewModel

class UserActivityViewModelFactory(
    private val app: Application,
    private val repository: UserActivityRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserActivityViewModel(app, repository) as T
    }
}