package com.example.yourday.viewmodel.goals_and_habits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalGoal
import com.example.yourday.repository.goals_and_habits.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalViewModel(
    application: Application,
    private val repository: GoalRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _goals = MutableStateFlow<List<LocalGoal>>(emptyList())
    val goals: StateFlow<List<LocalGoal>> = _goals.asStateFlow()

    fun loadGoals() {
        viewModelScope.launch {
            repository.getByUser(userId).collect { goalsList ->
                _goals.value = goalsList
            }
        }
    }

    fun upsertGoal(goal: LocalGoal) {
        viewModelScope.launch {
            repository.upsert(goal)
        }
    }

    fun deleteGoal(goal: LocalGoal) {
        viewModelScope.launch {
            repository.delete(goal)
        }
    }
}
