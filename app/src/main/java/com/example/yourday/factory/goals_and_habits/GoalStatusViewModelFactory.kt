package com.example.yourday.factory.goals_and_habits

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.goals_and_habits.GoalStatusRepository
import com.example.yourday.viewmodel.goals_and_habits.GoalStatusViewModel

class GoalStatusViewModelFactory {
    // Goal Statuses
    class GoalStatusViewModelFactory(
        val app: Application,
        private val repository: GoalStatusRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GoalStatusViewModel(app) as T
        }
    }
}