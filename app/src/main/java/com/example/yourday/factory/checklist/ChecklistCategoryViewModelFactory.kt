package com.example.yourday.factory.checklist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.checklist.ChecklistCategoryRepository
import com.example.yourday.viewmodel.checklist.ChecklistCategoryViewModel

class ChecklistCategoryViewModelFactory(
    val app: Application,
    private val repository: ChecklistCategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChecklistCategoryViewModel(app) as T
    }
}