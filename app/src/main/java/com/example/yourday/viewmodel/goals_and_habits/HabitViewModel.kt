package com.example.yourday.viewmodel.goals_and_habits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalHabit
import com.example.yourday.repository.goals_and_habits.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val userId = "local_user"
    val habits: MutableStateFlow<List<LocalHabit>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = HabitRepository(db)
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            repository.getByUser(userId).collect { habitsList ->
                habits.value = habitsList
            }
        }
    }

    fun upsertHabit(habit: LocalHabit) {
        viewModelScope.launch {
            repository.upsert(habit)
        }
    }

    fun deleteHabit(habit: LocalHabit) {
        viewModelScope.launch {
            repository.delete(habit)
        }
    }
}