package com.example.yourday.factory.motivation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.motivation.MotivationalCardRepository
import com.example.yourday.viewmodel.motivation.MotivationalCardViewModel

class MotivationalCardViewModelFactory(
    val app: Application,
    private val repository: MotivationalCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MotivationalCardViewModel(app,repository) as T
    }
}
