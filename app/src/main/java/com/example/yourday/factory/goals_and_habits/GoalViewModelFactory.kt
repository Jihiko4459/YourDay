package com.example.yourday.factory.goals_and_habits

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.goals_and_habits.GoalRepository
import com.example.yourday.viewmodel.goals_and_habits.GoalViewModel

class GoalViewModelFactory(
    val app: Application,
    private val repository: GoalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoalViewModel(app, repository) as T
    }
}