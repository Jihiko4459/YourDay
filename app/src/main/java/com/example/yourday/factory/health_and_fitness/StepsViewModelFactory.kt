package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.StepsRepository
import com.example.yourday.viewmodel.health_and_fitness.StepsViewModel

class StepsViewModelFactory(
    val app: Application,
    private val repository: StepsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StepsViewModel(app, repository) as T
    }
}
