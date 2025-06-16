package com.example.yourday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGender
import com.example.yourday.repository.GenderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GenderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GenderRepository
    val genders: MutableStateFlow<List<LocalGender>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = GenderRepository(db)
        loadGenders()
    }

    private fun loadGenders() {
        viewModelScope.launch {
            repository.getAll().collect { gendersList ->
                genders.value = gendersList
            }
        }
    }

    fun upsertGender(gender: LocalGender) {
        viewModelScope.launch {
            repository.upsert(gender)
        }
    }

    fun deleteGender(gender: LocalGender) {
        viewModelScope.launch {
            repository.delete(gender)
        }
    }

    // Функция для добавления всех справочных гендеров
    fun insertAllReferenceGenders() {
        viewModelScope.launch {
            val referenceGenders = listOf(
                LocalGender(1, "Женский"),
                LocalGender(2, "Мужской")
            )
            repository.insertAllReferenceGenders(referenceGenders)
        }
    }

    // Функция для удаления всех справочных гендеров
    fun deleteAllReferenceGenders() {
        viewModelScope.launch {
            repository.deleteAllReferenceGenders()
        }
    }
}