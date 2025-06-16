package com.example.yourday.viewmodel.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalHobby
import com.example.yourday.repository.daily.HobbyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HobbyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HobbyRepository
    private val userId = "local_user"
    val hobbies: MutableStateFlow<List<LocalHobby>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = HobbyRepository(db)
        loadHobbies()
    }

    private fun loadHobbies() {
        viewModelScope.launch {
            repository.getByUser(userId).collect { hobbiesList ->
                hobbies.value = hobbiesList
            }
        }
    }

    fun upsertHobby(hobby: LocalHobby) {
        viewModelScope.launch {
            repository.upsert(hobby)
        }
    }

    fun deleteHobby(hobby: LocalHobby) {
        viewModelScope.launch {
            repository.delete(hobby)
        }
    }
}