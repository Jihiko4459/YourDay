package com.example.yourday.viewmodel.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalChecklistItem
import com.example.yourday.repository.checklist.ChecklistItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChecklistItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChecklistItemRepository
    private val userId = "local_user"
    val items: MutableStateFlow<List<LocalChecklistItem>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = ChecklistItemRepository(db)
    }

    fun loadItemsByCategory(categoryId: Int) {
        viewModelScope.launch {
            repository.getByCategory(userId, categoryId).collect { itemsList ->
                items.value = itemsList
            }
        }
    }

    fun upsertItem(item: LocalChecklistItem) {
        viewModelScope.launch {
            repository.upsert(item)
        }
    }

    fun deleteItem(item: LocalChecklistItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}