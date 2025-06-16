package com.example.yourday.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.GenderRepository
import com.example.yourday.viewmodel.GenderViewModel

class GenderViewModelFactory(
    val app: Application,
    private val repository: GenderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GenderViewModel(app) as T
    }
}