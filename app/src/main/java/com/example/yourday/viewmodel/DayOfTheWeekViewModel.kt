package com.example.yourday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDayOfTheWeek
import com.example.yourday.repository.DayOfTheWeekRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DayOfTheWeekViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DayOfTheWeekRepository
    val days: MutableStateFlow<List<LocalDayOfTheWeek>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = DayOfTheWeekRepository(db)
        loadDays()
    }

    private fun loadDays() {
        viewModelScope.launch {
            repository.getAll().collect { daysList ->
                days.value = daysList
            }
        }
    }

    fun upsertDay(day: LocalDayOfTheWeek) {
        viewModelScope.launch {
            repository.upsert(day)
        }
    }

    fun deleteDay(day: LocalDayOfTheWeek) {
        viewModelScope.launch {
            repository.delete(day)
        }
    }


    // 1. Функция загрузки начальных данных (при регистрации/входе)
    fun loadInitialDays() {
        viewModelScope.launch {
            // Все дни недели из CSV файла
            val initialDays = listOf(
                LocalDayOfTheWeek(id = 1, dayWeeklyName = "Понедельник"),
                LocalDayOfTheWeek(id = 2, dayWeeklyName = "Вторник"),
                LocalDayOfTheWeek(id = 3, dayWeeklyName = "Среда"),
                LocalDayOfTheWeek(id = 4, dayWeeklyName = "Четверг"),
                LocalDayOfTheWeek(id = 5, dayWeeklyName = "Пятница"),
                LocalDayOfTheWeek(id = 6, dayWeeklyName = "Суббота"),
                LocalDayOfTheWeek(id = 7, dayWeeklyName = "Воскресенье")
            )

            // Очищаем старые данные
            repository.deleteAll()

            // Добавляем новые
            initialDays.forEach { day ->
                repository.upsert(day)
            }

            // Загружаем обновленный список
            loadDays()
        }
    }

    // 2. Функция очистки данных (при выходе)
    fun clearDays() {
        viewModelScope.launch {
            repository.deleteAll()
            days.value = emptyList()
        }
    }


}