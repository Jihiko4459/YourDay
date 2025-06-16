package com.example.yourday.viewmodel.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleInCategory
import com.example.yourday.repository.content.ArticleInCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArticleInCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleInCategoryRepository
    val articlesInCategory: MutableStateFlow<List<LocalArticleInCategory>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ArticleInCategoryRepository(db)
    }

    fun upsertRelation(relation: LocalArticleInCategory) {
        viewModelScope.launch {
            repository.upsert(relation)
        }
    }

    fun deleteRelation(relation: LocalArticleInCategory) {
        viewModelScope.launch {
            repository.delete(relation)
        }
    }

    // 1. Функция для загрузки начальных данных (при регистрации/входе)
    fun loadInitialReferenceData() {
        viewModelScope.launch {
            // Создаем список начальных данных (все записи до article_id=17)
            val initialData = listOf(
                LocalArticleInCategory(id = 1, articleId = 1, categoryId = 1),
                LocalArticleInCategory(id = 2, articleId = 2, categoryId = 1),
                LocalArticleInCategory(id = 3, articleId = 2, categoryId = 10),
                LocalArticleInCategory(id = 4, articleId = 2, categoryId = 5),
                LocalArticleInCategory(id = 5, articleId = 3, categoryId = 1),
                LocalArticleInCategory(id = 6, articleId = 3, categoryId = 10),
                LocalArticleInCategory(id = 7, articleId = 4, categoryId = 2),
                LocalArticleInCategory(id = 8, articleId = 4, categoryId = 5),
                LocalArticleInCategory(id = 9, articleId = 5, categoryId = 2),
                LocalArticleInCategory(id = 10, articleId = 5, categoryId = 10),
                LocalArticleInCategory(id = 11, articleId = 6, categoryId = 2),
                LocalArticleInCategory(id = 12, articleId = 6, categoryId = 3),
                LocalArticleInCategory(id = 13, articleId = 7, categoryId = 4),
                LocalArticleInCategory(id = 14, articleId = 7, categoryId = 1),
                LocalArticleInCategory(id = 15, articleId = 8, categoryId = 4),
                LocalArticleInCategory(id = 16, articleId = 8, categoryId = 5),
                LocalArticleInCategory(id = 17, articleId = 9, categoryId = 4),
                LocalArticleInCategory(id = 18, articleId = 9, categoryId = 10),
                LocalArticleInCategory(id = 19, articleId = 10, categoryId = 5),
                LocalArticleInCategory(id = 20, articleId = 10, categoryId = 8),
                LocalArticleInCategory(id = 21, articleId = 11, categoryId = 7),
                LocalArticleInCategory(id = 22, articleId = 11, categoryId = 5),
                LocalArticleInCategory(id = 23, articleId = 12, categoryId = 7),
                LocalArticleInCategory(id = 24, articleId = 12, categoryId = 5),
                LocalArticleInCategory(id = 25, articleId = 13, categoryId = 8),
                LocalArticleInCategory(id = 26, articleId = 13, categoryId = 4),
                LocalArticleInCategory(id = 27, articleId = 14, categoryId = 8),
                LocalArticleInCategory(id = 28, articleId = 14, categoryId = 2),
                LocalArticleInCategory(id = 29, articleId = 15, categoryId = 9),
                LocalArticleInCategory(id = 30, articleId = 15, categoryId = 4),
                LocalArticleInCategory(id = 31, articleId = 16, categoryId = 10),
                LocalArticleInCategory(id = 32, articleId = 16, categoryId = 5),
                LocalArticleInCategory(id = 33, articleId = 17, categoryId = 10),
                LocalArticleInCategory(id = 34, articleId = 17, categoryId = 2)
            )

            // Очищаем старые данные (если есть)
            repository.deleteAll()

            // Добавляем новые начальные данные
            initialData.forEach { relation ->
                repository.upsert(relation)
            }

            // Обновляем список в MutableStateFlow
            repository.getAll().collect { relations ->
                articlesInCategory.value = relations
            }
        }
    }

    // 2. Функция для очистки данных при выходе
    fun clearReferenceData() {
        viewModelScope.launch {
            repository.deleteAll()
            articlesInCategory.value = emptyList()
        }
    }


    // Загрузка данных по категории
    fun loadByCategory(categoryId: Int) {
        viewModelScope.launch {
            repository.getByCategory(categoryId).collect { relations ->
                articlesInCategory.value = relations
            }
        }
    }

    // Загрузка всех данных
    fun loadAll() {
        viewModelScope.launch {
            repository.getAll().collect { relations ->
                articlesInCategory.value = relations
            }
        }
    }

}
