package com.example.yourday.viewmodel.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDailyNote
import com.example.yourday.repository.daily.DailyNoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DailyNoteViewModel(
    application: Application,
    private val repository: DailyNoteRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    val dailyNotes: MutableStateFlow<List<LocalDailyNote>> = MutableStateFlow(emptyList())
    init {
        val db = YourDayDatabase.getDatabase(application)
    }

    fun loadNotes(date: String) {
        viewModelScope.launch {
            repository.getNotesByDate(userId, date).collect { notes ->
                dailyNotes.value = notes
            }
        }
    }

    fun upsertNote(note: LocalDailyNote) {
        viewModelScope.launch {
            repository.upsert(note)
        }
    }

    fun deleteNote(note: LocalDailyNote) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}