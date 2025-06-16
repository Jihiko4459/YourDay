package com.example.yourday.viewmodel.daily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import com.example.yourday.repository.daily.GratitudeAndJoyJournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GratitudeAndJoyJournalViewModel(
    application: Application,
    private val repository: GratitudeAndJoyJournalRepository
) : AndroidViewModel(application) {
    private val userId = "local_user"
    private val _journal = MutableStateFlow<List<LocalGratitudeAndJoyJournal>>(emptyList())
    val journal: StateFlow<List<LocalGratitudeAndJoyJournal>> = _journal.asStateFlow()

    fun loadJournalByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { journalData ->
                _journal.value = listOfNotNull(journalData)
            }
        }
    }

    fun upsertJournal(journal: LocalGratitudeAndJoyJournal) {
        viewModelScope.launch {
            repository.upsert(journal)
        }
    }

    fun deleteJournal(journal: LocalGratitudeAndJoyJournal) {
        viewModelScope.launch {
            repository.delete(journal)
        }
    }
}
