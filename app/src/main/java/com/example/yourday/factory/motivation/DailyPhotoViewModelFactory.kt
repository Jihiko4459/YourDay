package com.example.yourday.factory.motivation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.motivation.DailyPhotoRepository
import com.example.yourday.viewmodel.motivation.DailyPhotoViewModel

class DailyPhotoViewModelFactory(
    val app: Application,
    private val repository: DailyPhotoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyPhotoViewModel(app,repository) as T
    }
}