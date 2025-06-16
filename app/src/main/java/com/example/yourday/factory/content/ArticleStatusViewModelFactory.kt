package com.example.yourday.factory.content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.content.ArticleStatusRepository
import com.example.yourday.viewmodel.content.ArticleStatusViewModel

class ArticleStatusViewModelFactory(
    val app: Application,
    private val repository: ArticleStatusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleStatusViewModel(app) as T
    }
}