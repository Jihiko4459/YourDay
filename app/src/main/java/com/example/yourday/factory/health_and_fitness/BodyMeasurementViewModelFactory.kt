package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.BodyMeasurementRepository
import com.example.yourday.viewmodel.health_and_fitness.BodyMeasurementViewModel

class BodyMeasurementViewModelFactory(
    val app: Application,
    private val repository: BodyMeasurementRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BodyMeasurementViewModel(app,repository) as T
    }
}