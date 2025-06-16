package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalWaterIntake
import com.example.yourday.repository.health_and_fitness.WaterIntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaterIntakeViewModel(
    application: Application,
    private val repository: WaterIntakeRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _intakes = MutableStateFlow<List<LocalWaterIntake>>(emptyList())
    val intakes: StateFlow<List<LocalWaterIntake>> = _intakes.asStateFlow()

    fun loadIntakesByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { intakesList ->
                _intakes.value = intakesList
            }
        }
    }

    fun upsertIntake(intake: LocalWaterIntake) {
        viewModelScope.launch {
            repository.upsert(intake)
        }
    }

    fun deleteIntake(intake: LocalWaterIntake) {
        viewModelScope.launch {
            repository.delete(intake)
        }
    }
}