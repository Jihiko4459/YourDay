package com.example.yourday.viewmodel.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalIdea
import com.example.yourday.repository.daily.IdeaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IdeaViewModel(
    application: Application,
    private val repository: IdeaRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _ideas = MutableStateFlow<List<LocalIdea>>(emptyList())
    val ideas: StateFlow<List<LocalIdea>> = _ideas.asStateFlow()

    fun loadIdeasByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { ideasList ->
                _ideas.value = ideasList
            }
        }
    }

    fun upsertIdea(idea: LocalIdea) {
        viewModelScope.launch {
            repository.upsert(idea)
        }
    }

    fun deleteIdea(idea: LocalIdea) {
        viewModelScope.launch {
            repository.delete(idea)
        }
    }
}