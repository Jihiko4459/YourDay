package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalNutritionLog
import com.example.yourday.repository.health_and_fitness.NutritionLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

class NutritionLogViewModel(
    application: Application,
    private val repository: NutritionLogRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _nutritionLogs = MutableStateFlow<List<LocalNutritionLog>>(emptyList())
    val nutritionLogs: StateFlow<List<LocalNutritionLog>> = _nutritionLogs.asStateFlow()

    fun loadLogsByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { logs ->
                _nutritionLogs.value = logs
            }
        }
    }

    fun upsertLog(log: LocalNutritionLog) {
        viewModelScope.launch {
            repository.upsert(log)
        }
    }

    fun deleteLog(log: LocalNutritionLog) {
        viewModelScope.launch {
            repository.delete(log)
        }
    }
}