package com.example.yourday.viewmodel.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalUserArticleStatus
import com.example.yourday.repository.content.UserArticleStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserArticleStatusViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserArticleStatusRepository
    private val userId = "local_user"
    val articleStatus: MutableStateFlow<LocalUserArticleStatus?> = MutableStateFlow(null)

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = UserArticleStatusRepository(db)
    }

    fun loadStatusForArticle(articleId: Int) {
        viewModelScope.launch {
            repository.getByArticle(userId, articleId).collect { status ->
                articleStatus.value = status
            }
        }
    }

    fun upsertStatus(status: LocalUserArticleStatus) {
        viewModelScope.launch {
            repository.upsert(status)
        }
    }

    fun deleteStatus(status: LocalUserArticleStatus) {
        viewModelScope.launch {
            repository.delete(status)
        }
    }
}