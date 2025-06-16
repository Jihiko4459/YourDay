package com.example.yourday.viewmodel.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalItemMark
import com.example.yourday.repository.checklist.ItemMarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ItemMarkViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemMarkRepository
    private val userId = "local_user"
    val itemMarks: MutableStateFlow<List<LocalItemMark>> = MutableStateFlow(emptyList())

    init {
        val db by lazy {
            Room.databaseBuilder(
                application,//передаем контекст приложения
                YourDayDatabase::class.java,//и класс бд
                "notes.db"//название бд
            ).build()
        }//создаем объект бд
        repository = ItemMarkRepository(db)
    }

    fun loadMarksByDate(date: String) {
        viewModelScope.launch {
            repository.getByDate(userId, date).collect { marks ->
                itemMarks.value = marks
            }
        }
    }

    fun upsertMark(mark: LocalItemMark) {
        viewModelScope.launch {
            repository.upsert(mark)
        }
    }

    fun deleteMark(mark: LocalItemMark) {
        viewModelScope.launch {
            repository.delete(mark)
        }
    }
}