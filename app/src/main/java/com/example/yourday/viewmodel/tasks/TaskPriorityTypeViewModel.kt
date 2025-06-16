package com.example.yourday.viewmodel.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskPriorityType
import com.example.yourday.repository.tasks.TaskPriorityTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskPriorityTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskPriorityTypeRepository
    val priorityTypes: MutableStateFlow<List<LocalTaskPriorityType>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = TaskPriorityTypeRepository(db)
        loadPriorityTypes()
    }

    private fun loadPriorityTypes() {
        viewModelScope.launch {
            repository.getAll().collect { types ->
                priorityTypes.value = types
            }
        }
    }

    fun upsertPriorityType(priorityType: LocalTaskPriorityType) {
        viewModelScope.launch {
            repository.upsert(priorityType)
        }
    }

    fun deletePriorityType(priorityType: LocalTaskPriorityType) {
        viewModelScope.launch {
            repository.delete(priorityType)
        }
    }
}