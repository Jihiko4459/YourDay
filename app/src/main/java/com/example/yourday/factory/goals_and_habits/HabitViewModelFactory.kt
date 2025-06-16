package com.example.yourday.factory.goals_and_habits

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.goals_and_habits.HabitRepository
import com.example.yourday.viewmodel.goals_and_habits.HabitViewModel

// Habits
class HabitViewModelFactory(
    val app: Application,
    private val repository: HabitRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitViewModel(app) as T
    }
}