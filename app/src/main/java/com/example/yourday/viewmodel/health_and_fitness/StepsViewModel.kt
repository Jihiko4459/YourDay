package com.example.yourday.viewmodel.health_and_fitness

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalSteps
import com.example.yourday.repository.health_and_fitness.StepsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StepsViewModel(
    application: Application,
    private val repository: StepsRepository,
    private val userId: String? = "local_user"
) : AndroidViewModel(application) {
    private val _steps = MutableStateFlow<List<LocalSteps>>(emptyList())
    val steps: StateFlow<List<LocalSteps>> = _steps

    // Для запроса разрешений
    val permissionNeeded = MutableLiveData<Boolean>()

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var initialSteps = -1f
    private var isTracking = false

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    if (initialSteps < 0) {
                        initialSteps = event.values[0]
                    }
                    val currentSteps = event.values[0] - initialSteps
                    updateSteps(currentSteps.toInt())
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.d("StepsViewModel", "Accuracy changed: $accuracy")
        }
    }

    init {
        sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.e("StepsViewModel", "Step Counter sensor NOT AVAILABLE on this device")
        } else {
            Log.d("StepsViewModel", "Step Counter sensor available: ${stepSensor?.name}")
        }
    }

    fun startStepTracking() {
        if (isTracking || stepSensor == null) return

        try {
            val success = sensorManager?.registerListener(
                sensorEventListener,
                stepSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            if (success == true) {
                isTracking = true
                Log.d("StepsViewModel", "Successfully registered sensor listener")
            } else {
                Log.e("StepsViewModel", "Failed to register sensor listener")
            }
        } catch (e: Exception) {
            Log.e("StepsViewModel", "Sensor registration error: ${e.message}")
        }
    }

    fun stopStepTracking() {
        if (!isTracking) return

        try {
            sensorManager?.unregisterListener(sensorEventListener)
            isTracking = false
            Log.d("StepsViewModel", "Sensor listener unregistered")
        } catch (e: Exception) {
            Log.e("StepsViewModel", "Sensor unregistration error: ${e.message}")
        }
    }

    fun startTrackingWithPermissionsCheck() {
        if (hasActivityRecognitionPermission()) {
            startStepTracking()
        } else {
            // Уведомляем Activity/Fragment о необходимости запросить разрешение
            permissionNeeded.postValue(true)
        }
    }

    private fun hasActivityRecognitionPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun updateSteps(newCount: Int) {
        viewModelScope.launch {
            val current = _steps.value.firstOrNull() ?: LocalSteps(
                userId = userId.toString(),
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                stepsCount = 0
            )
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
}