package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalUserActivity
import com.example.yourday.repository.health_and_fitness.UserActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserActivityViewModel(
    application: Application,
    private val repository: UserActivityRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _activities = MutableStateFlow<List<LocalUserActivity>>(emptyList())
    val activities: StateFlow<List<LocalUserActivity>> = _activities.asStateFlow()

    fun loadActivitiesByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { activitiesList ->
                _activities.value = activitiesList
            }
        }
    }

    fun upsertActivity(activity: LocalUserActivity) {
        viewModelScope.launch {
            repository.upsert(activity)
        }
    }

    fun deleteActivity(activity: LocalUserActivity) {
        viewModelScope.launch {
            repository.delete(activity)
        }
    }
}
