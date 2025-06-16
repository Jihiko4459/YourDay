package com.example.yourday.factory.content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.content.ArticleRepository
import com.example.yourday.viewmodel.content.ArticleViewModel

class ArticleViewModelFactory(
    val app: Application,
    private val repository: ArticleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleViewModel(app) as T
    }
}