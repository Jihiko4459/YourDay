package com.example.yourday.factory.motivation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.motivation.DailyQuoteRepository
import com.example.yourday.viewmodel.motivation.DailyQuoteViewModel

class DailyQuoteViewModelFactory(
    val app: Application,
    private val repository: DailyQuoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyQuoteViewModel(app,repository) as T
    }
}