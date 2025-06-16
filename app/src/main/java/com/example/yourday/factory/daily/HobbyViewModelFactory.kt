package com.example.yourday.factory.daily

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.daily.HobbyRepository
import com.example.yourday.viewmodel.daily.HobbyViewModel

class HobbyViewModelFactory(
    val app: Application,
    private val repository: HobbyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HobbyViewModel(app) as T
    }
}