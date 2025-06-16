package com.example.yourday.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.DayOfTheWeekRepository
import com.example.yourday.viewmodel.DayOfTheWeekViewModel

class DayOfTheWeekViewModelFactory(
    val app: Application,
    private val repository: DayOfTheWeekRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DayOfTheWeekViewModel(app) as T
    }
}