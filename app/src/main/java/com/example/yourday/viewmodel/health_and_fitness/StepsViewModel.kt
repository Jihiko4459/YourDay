package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalSteps
import com.example.yourday.repository.health_and_fitness.StepsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StepsViewModel(
    application: Application,
    private val repository: StepsRepository,
    private val userId: String? ="local_user"// Добавляем userId как параметр
) : AndroidViewModel(application) {
    private val _steps = MutableStateFlow<List<LocalSteps>>(emptyList())
    val steps: StateFlow<List<LocalSteps>> = _steps

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var stepCount = 0

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            event.values?.firstOrNull()?.let { value ->
                stepCount = value.toInt()
                updateSteps(stepCount)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // Можно добавить обработку изменения точности, если нужно
        }
    }

    init {
        sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun startStepTracking() {
        stepSensor?.let { sensor ->
            sensorManager?.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopStepTracking() {
        sensorManager?.unregisterListener(sensorEventListener)
    }

    fun updateSteps(newCount: Int) {
        viewModelScope.launch {
            val current = _steps.value.firstOrNull() ?: return@launch
            val updated = current.copy(stepsCount = newCount)
            repository.upsert(updated)
            _steps.value = listOf(updated)
        }
    }

    fun loadStepsByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId.toString(), date).collect { stepsData ->
                _steps.value = if (stepsData != null) listOf(stepsData)
                else listOf(LocalSteps(userId = userId.toString(), date = date, stepsCount = 0))
            }
        }
    }

    fun incrementSteps(increment: Int = 1) {
        viewModelScope.launch {
            val current = _steps.value.firstOrNull() ?: return@launch
            val updated = current.copy(stepsCount = current.stepsCount + increment)
            repository.upsert(updated)
            _steps.value = listOf(updated)
        }
    }
}
