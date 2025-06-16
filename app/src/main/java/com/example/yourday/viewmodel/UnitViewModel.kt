package com.example.yourday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalUnit
import com.example.yourday.repository.UnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UnitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UnitRepository
    val units: MutableStateFlow<List<LocalUnit>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = UnitRepository(db)
        loadUnits()
    }

    private fun loadUnits() {
        viewModelScope.launch {
            repository.getAll().collect { unitsList ->
                units.value = unitsList
            }
        }
    }

    fun upsertUnit(unit: LocalUnit) {
        viewModelScope.launch {
            repository.upsert(unit)
        }
    }

    fun deleteUnit(unit: LocalUnit) {
        viewModelScope.launch {
            repository.delete(unit)
        }
    }
}