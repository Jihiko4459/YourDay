package com.example.yourday.factory.daily

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.daily.GratitudeAndJoyJournalRepository
import com.example.yourday.viewmodel.daily.GratitudeAndJoyJournalViewModel

class GratitudeAndJoyJournalViewModelFactory(
    val app: Application,
    private val repository: GratitudeAndJoyJournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GratitudeAndJoyJournalViewModel(app, repository) as T
    }
}
