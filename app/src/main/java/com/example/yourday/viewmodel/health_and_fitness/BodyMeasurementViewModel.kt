package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalBodyMeasurement
import com.example.yourday.repository.health_and_fitness.BodyMeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BodyMeasurementViewModel(
    application: Application,
    private val repository: BodyMeasurementRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _measurements = MutableStateFlow<List<LocalBodyMeasurement>>(emptyList())
    val measurements: StateFlow<List<LocalBodyMeasurement>> = _measurements.asStateFlow()

    fun loadMeasurementByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { measurementData ->
                measurementData?.let {
                    _measurements.value = listOf(it)
                } ?: run {
                    _measurements.value = emptyList()
                }
            }
        }
    }

    fun upsertMeasurement(measurement: LocalBodyMeasurement) {
        viewModelScope.launch {
            repository.upsert(measurement)
        }
    }

    fun deleteMeasurement(measurement: LocalBodyMeasurement) {
        viewModelScope.launch {
            repository.delete(measurement)
        }
    }
}
