package com.example.yourday.factory.content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.content.ArticleInCategoryRepository

class ArticleInCategoryViewModelFactory(
    val app: Application,
    private val repository: ArticleInCategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return com.example.yourday.viewmodel.content.ArticleInCategoryViewModel(app) as T
    }
}
