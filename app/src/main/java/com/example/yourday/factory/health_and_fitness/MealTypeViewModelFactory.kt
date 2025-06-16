package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.MealTypeRepository
import com.example.yourday.viewmodel.health_and_fitness.MealTypeViewModel

class MealTypeViewModelFactory(
    val app: Application,
    private val repository: MealTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealTypeViewModel(app) as T
    }
}