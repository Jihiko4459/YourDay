package com.example.yourday.factory.checklist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.checklist.ItemMarkRepository
import com.example.yourday.viewmodel.checklist.ItemMarkViewModel

class ItemMarkViewModelFactory(
    val app: Application,
    private val repository: ItemMarkRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemMarkViewModel(app) as T
    }
}