package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.WaterIntakeRepository
import com.example.yourday.viewmodel.health_and_fitness.WaterIntakeViewModel

class WaterIntakeViewModelFactory(
    val app: Application,
    private val repository: WaterIntakeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaterIntakeViewModel(app,repository) as T
    }
}