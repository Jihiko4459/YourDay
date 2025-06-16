package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.FoodItemRepository
import com.example.yourday.viewmodel.health_and_fitness.FoodItemViewModel

class FoodItemViewModelFactory(
    val app: Application,
    private val repository: FoodItemRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FoodItemViewModel(app) as T
    }
}