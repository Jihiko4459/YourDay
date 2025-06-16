package com.example.yourday.viewmodel.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalChecklistCategory
import com.example.yourday.repository.checklist.ChecklistCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChecklistCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChecklistCategoryRepository
    val categories: MutableStateFlow<List<LocalChecklistCategory>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ChecklistCategoryRepository(db)
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAll().collect { categoriesList ->
                categories.value = categoriesList
            }
        }
    }

    fun upsertCategory(category: LocalChecklistCategory) {
        viewModelScope.launch {
            repository.upsert(category)
        }
    }

    fun deleteCategory(category: LocalChecklistCategory) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }


    // 1. Функция загрузки начальных данных (при регистрации/входе)
    fun loadInitialCategories() {
        viewModelScope.launch {
            // Все категории из CSV файла
            val initialCategories = listOf(
                LocalChecklistCategory(1, "Документы", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//Icon.svg"),
                LocalChecklistCategory(2, "Финансы", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//dollar-sign.svg"),
                LocalChecklistCategory(3, "Электроника", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//smartphone.svg"),
                LocalChecklistCategory(4, "Личные вещи", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//glasses-outline.svg"),
                LocalChecklistCategory(5, "Работа/учеба", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//Icon-1.svg"),
                LocalChecklistCategory(6, "Здоровье и гигиена", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//soap-bubble.svg"),
                LocalChecklistCategory(7, "Для детей", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//baby.svg"),
                LocalChecklistCategory(8, "Спорт и активность", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//dumbbells_0rljf1lyt3gw%201.svg"),
                LocalChecklistCategory(9, "Творчество", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//theatre_xhpbtryeeih0%201.svg"),
                LocalChecklistCategory(10, "Специальные случаи", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/checklist-category-type//info.svg")
            )

            // Очищаем старые данные
            repository.deleteAll()

            // Добавляем новые
            initialCategories.forEach { category ->
                repository.upsert(category)
            }

            // Загружаем обновленный список
            loadCategories()
        }
    }

    // 2. Функция очистки данных (при выходе)
    fun clearCategories() {
        viewModelScope.launch {
            repository.deleteAll()
            categories.value = emptyList()
        }
    }
}