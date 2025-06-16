package com.example.yourday.factory.goals_and_habits

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.goals_and_habits.GoalAndHabitTypeRepository
import com.example.yourday.viewmodel.goals_and_habits.GoalAndHabitTypeViewModel

class GoalAndHabitTypeViewModelFactory(
    val app: Application,
    private val repository: GoalAndHabitTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoalAndHabitTypeViewModel(app) as T
    }
}