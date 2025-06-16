package com.example.yourday.factory.content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.content.ArticleCategoryRepository
import com.example.yourday.viewmodel.content.ArticleCategoryViewModel

class ArticleCategoryViewModelFactory(
    val app: Application,
    private val repository: ArticleCategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleCategoryViewModel(app) as T
    }
}