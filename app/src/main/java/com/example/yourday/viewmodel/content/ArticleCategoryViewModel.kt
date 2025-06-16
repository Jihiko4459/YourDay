package com.example.yourday.viewmodel.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalArticleCategory
import com.example.yourday.repository.content.ArticleCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArticleCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ArticleCategoryRepository
    val categories: MutableStateFlow<List<LocalArticleCategory>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ArticleCategoryRepository(db)
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAll().collect { categoriesList ->
                categories.value = categoriesList
            }
        }
    }

    fun upsertCategory(category: LocalArticleCategory) {
        viewModelScope.launch {
            repository.upsert(category)
        }
    }

    fun deleteCategory(category: LocalArticleCategory) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }

    // Добавляем метод для загрузки справочных данных
    fun addDefaultCategories() {
        viewModelScope.launch {
            val defaultCategories = listOf(
                LocalArticleCategory(
                    id = 1,
                    categoryName = "Планирование и продуктивность"
                ),
                LocalArticleCategory(
                    id = 2,
                    categoryName = "Здоровье и спорт"
                ),
                LocalArticleCategory(
                    id = 3,
                    categoryName = "Питание"
                ),
                LocalArticleCategory(
                    id = 4,
                    categoryName = "Финансы"
                ),
                LocalArticleCategory(
                    id = 5,
                    categoryName = "Саморазвитие"
                ),
                LocalArticleCategory(
                    id = 6,
                    categoryName = "Творчество и хобби"
                ),
                LocalArticleCategory(
                    id = 7,
                    categoryName = "Отношения и коммуникация"
                ),
                LocalArticleCategory(
                    id = 8,
                    categoryName = "Технологии и гаджеты"
                ),
                LocalArticleCategory(
                    id = 9,
                    categoryName = "Путешествия и образ жизни"
                ),
                LocalArticleCategory(
                    id = 10,
                    categoryName = "Психология и мотивация"
                ),
                LocalArticleCategory(
                    id = 11,
                    categoryName = "Специальная категория"
                )
            )

            // Сначала очищаем существующие категории
            repository.deleteAll()

            // Добавляем все категории
            defaultCategories.forEach { category ->
                repository.upsert(category)
            }
        }
    }

    // Метод для очистки категорий при выходе
    fun clearCategories() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}