package com.example.yourday.viewmodel.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleStatus
import com.example.yourday.repository.content.ArticleStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArticleStatusViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleStatusRepository
    val statuses: MutableStateFlow<List<LocalArticleStatus>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ArticleStatusRepository(db)
        loadStatuses()
    }

    private fun loadStatuses() {
        viewModelScope.launch {
            repository.getAll().collect { statusesList ->
                statuses.value = statusesList
            }
        }
    }

    fun upsertStatus(status: LocalArticleStatus) {
        viewModelScope.launch {
            repository.upsert(status)
        }
    }

    fun deleteStatus(status: LocalArticleStatus) {
        viewModelScope.launch {
            repository.delete(status)
        }
    }


    // Добавляем метод для загрузки справочных данных
    fun addDefaultStatuses() {
        viewModelScope.launch {
            val defaultStatuses = listOf(
                LocalArticleStatus(
                    id = 1,
                    statusName = "Не прочитана"
                ),
                LocalArticleStatus(
                    id = 2,
                    statusName = "Прочитана"
                )
            )

            // Сначала очищаем существующие статусы
            repository.deleteAll()

            // Добавляем все статусы
            defaultStatuses.forEach { status ->
                repository.upsert(status)
            }
        }
    }

    // Метод для очистки статусов при выходе
    fun clearStatuses() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }


}