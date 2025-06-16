package com.example.yourday.viewmodel.finance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalBalanceType
import com.example.yourday.repository.finance.BalanceTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BalanceTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BalanceTypeRepository
    val balanceTypes: MutableStateFlow<List<LocalBalanceType>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = BalanceTypeRepository(db)
        loadBalanceTypes()
    }

    private fun loadBalanceTypes() {
        viewModelScope.launch {
            repository.getAll().collect { types ->
                balanceTypes.value = types
            }
        }
    }

    fun upsertBalanceType(balanceType: LocalBalanceType) {
        viewModelScope.launch {
            repository.upsert(balanceType)
        }
    }

    fun deleteBalanceType(balanceType: LocalBalanceType) {
        viewModelScope.launch {
            repository.delete(balanceType)
        }
    }

    // 1. Функция загрузки начальных данных (при регистрации/входе)
    fun loadInitialBalanceTypes() {
        viewModelScope.launch {
            // Создаем справочные данные
            val initialTypes = listOf(
                LocalBalanceType(id = 1, typeName = "Доход"),
                LocalBalanceType(id = 2, typeName = "Расход")
            )

            // Очищаем старые данные
            repository.deleteAll()

            // Добавляем новые
            initialTypes.forEach { type ->
                repository.upsert(type)
            }

            // Загружаем обновленный список
            loadBalanceTypes()
        }
    }

    // 2. Функция очистки данных (при выходе)
    fun clearBalanceTypes() {
        viewModelScope.launch {
            repository.deleteAll()
            balanceTypes.value = emptyList()
        }
    }
}