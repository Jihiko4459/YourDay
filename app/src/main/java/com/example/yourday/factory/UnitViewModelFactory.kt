package com.example.yourday.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.UnitRepository
import com.example.yourday.viewmodel.UnitViewModel

class UnitViewModelFactory(
    val app: Application,
    private val repository: UnitRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UnitViewModel(app) as T
    }
}