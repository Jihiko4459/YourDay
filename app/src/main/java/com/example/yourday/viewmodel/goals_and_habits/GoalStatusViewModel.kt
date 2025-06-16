package com.example.yourday.viewmodel.goals_and_habits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGoalStatus
import com.example.yourday.repository.goals_and_habits.GoalStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GoalStatusViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GoalStatusRepository
    val goalStatuses: MutableStateFlow<List<LocalGoalStatus>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = GoalStatusRepository(db)
        loadGoalStatuses()
    }

    private fun loadGoalStatuses() {
        viewModelScope.launch {
            repository.getAll().collect { statuses ->
                goalStatuses.value = statuses
            }
        }
    }

    fun upsertGoalStatus(status: LocalGoalStatus) {
        viewModelScope.launch {
            repository.upsert(status)
        }
    }

    fun deleteGoalStatus(status: LocalGoalStatus) {
        viewModelScope.launch {
            repository.delete(status)
        }
    }

    // Новая функция для добавления справочных статусов
    fun insertAllReferenceStatuses() {
        viewModelScope.launch {
            val referenceStatuses = listOf(
                LocalGoalStatus(1, "Не начата"),
                LocalGoalStatus(2, "В процессе"),
                LocalGoalStatus(3, "Завершена")
            )
            repository.insertAllReferenceStatuses(referenceStatuses)
        }
    }

    // Новая функция для удаления справочных статусов
    fun deleteAllReferenceStatuses() {
        viewModelScope.launch {
            repository.deleteAllReferenceStatuses()
        }
    }
}