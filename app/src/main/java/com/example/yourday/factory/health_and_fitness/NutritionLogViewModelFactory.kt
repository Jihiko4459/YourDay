package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.NutritionLogRepository
import com.example.yourday.viewmodel.health_and_fitness.NutritionLogViewModel

class NutritionLogViewModelFactory(
    val app: Application,
    private val repository: NutritionLogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NutritionLogViewModel(app,repository) as T
    }
}