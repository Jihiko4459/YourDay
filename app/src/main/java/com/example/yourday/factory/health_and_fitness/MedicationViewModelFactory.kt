package com.example.yourday.factory.health_and_fitness

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.health_and_fitness.MedicationRepository
import com.example.yourday.viewmodel.health_and_fitness.MedicationViewModel

class MedicationViewModelFactory(
    val app: Application,
    private val repository: MedicationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MedicationViewModel(app) as T
    }
}