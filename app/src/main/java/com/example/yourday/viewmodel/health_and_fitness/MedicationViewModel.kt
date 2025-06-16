package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalMedication
import com.example.yourday.repository.health_and_fitness.MedicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MedicationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MedicationRepository
    private val userId = "local_user"
    val medications: MutableStateFlow<List<LocalMedication>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = MedicationRepository(db)
        loadMedications()
    }

    private fun loadMedications() {
        viewModelScope.launch {
            repository.getByUser(userId).collect { meds ->
                medications.value = meds
            }
        }
    }

    fun upsertMedication(medication: LocalMedication) {
        viewModelScope.launch {
            repository.upsert(medication)
        }
    }

    fun deleteMedication(medication: LocalMedication) {
        viewModelScope.launch {
            repository.delete(medication)
        }
    }
}