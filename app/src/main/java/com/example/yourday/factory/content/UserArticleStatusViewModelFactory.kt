package com.example.yourday.factory.content

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.content.UserArticleStatusRepository
import com.example.yourday.viewmodel.content.UserArticleStatusViewModel

class UserArticleStatusViewModelFactory(
    val app: Application,
    private val repository: UserArticleStatusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserArticleStatusViewModel(app) as T
    }
}