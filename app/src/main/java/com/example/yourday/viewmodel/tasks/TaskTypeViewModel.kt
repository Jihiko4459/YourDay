package com.example.yourday.viewmodel.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTaskType
import com.example.yourday.repository.tasks.TaskTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskTypeRepository
    val taskTypes: MutableStateFlow<List<LocalTaskType>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = TaskTypeRepository(db)
        loadTaskTypes()
    }

    private fun loadTaskTypes() {
        viewModelScope.launch {
            repository.getAll().collect { types ->
                taskTypes.value = types
            }
        }
    }

    fun upsertTaskType(taskType: LocalTaskType) {
        viewModelScope.launch {
            repository.upsert(taskType)
        }
    }

    fun deleteTaskType(taskType: LocalTaskType) {
        viewModelScope.launch {
            repository.delete(taskType)
        }
    }
}