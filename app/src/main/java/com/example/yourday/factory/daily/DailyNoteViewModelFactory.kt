package com.example.yourday.factory.daily

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.daily.DailyNoteRepository
import com.example.yourday.viewmodel.daily.DailyNoteViewModel

class DailyNoteViewModelFactory(
    private val app: Application,
    private val repository: DailyNoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyNoteViewModel(app, repository) as T
    }
}