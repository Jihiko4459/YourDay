package com.example.yourday.factory.checklist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.checklist.ChecklistItemRepository
import com.example.yourday.viewmodel.checklist.ChecklistItemViewModel

class ChecklistItemViewModelFactory(
    val app: Application,
    private val repository: ChecklistItemRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChecklistItemViewModel(app) as T
    }
}