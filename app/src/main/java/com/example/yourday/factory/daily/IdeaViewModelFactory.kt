package com.example.yourday.factory.daily

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourday.repository.daily.IdeaRepository
import com.example.yourday.viewmodel.daily.IdeaViewModel

class IdeaViewModelFactory(
    val app: Application,
    private val repository: IdeaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return IdeaViewModel(app, repository) as T
    }
}
