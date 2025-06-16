package com.example.yourday.viewmodel.health_and_fitness

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalMealType
import com.example.yourday.repository.health_and_fitness.MealTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MealTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MealTypeRepository
    val mealTypes: MutableStateFlow<List<LocalMealType>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = MealTypeRepository(db)
        loadMealTypes()
    }

    private fun loadMealTypes() {
        viewModelScope.launch {
            repository.getAll().collect { types ->
                mealTypes.value = types
            }
        }
    }

    fun upsertMealType(mealType: LocalMealType) {
        viewModelScope.launch {
            repository.upsert(mealType)
        }
    }

    fun deleteMealType(mealType: LocalMealType) {
        viewModelScope.launch {
            repository.delete(mealType)
        }
    }

    // Добавление справочных типов
    fun insertAllReferenceTypes() {
        viewModelScope.launch {
            val referenceTypes = listOf(
                LocalMealType(1, "Завтрак"),
                LocalMealType(2, "Обед"),
                LocalMealType(3, "Ужин"),
                LocalMealType(4, "Перекус")
            )
            repository.insertAllReferenceTypes(referenceTypes)
        }
    }

    // Удаление справочных типов
    fun deleteAllReferenceTypes() {
        viewModelScope.launch {
            repository.deleteAllReferenceTypes()
        }
    }

}