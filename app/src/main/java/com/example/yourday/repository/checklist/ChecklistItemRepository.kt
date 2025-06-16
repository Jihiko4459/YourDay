package com.example.yourday.repository.checklist

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalChecklistItem
import kotlinx.coroutines.flow.Flow

class ChecklistItemRepository(private val db: YourDayDatabase) {
    fun getByCategory(userId: String, categoryId: Int): Flow<List<LocalChecklistItem>> {
        return db.checklistItemDao().getByCategory(userId, categoryId)
    }

    suspend fun getById(id: Int): LocalChecklistItem? {
        return db.checklistItemDao().getById(id)
    }
    suspend fun upsert(item: LocalChecklistItem) {
        db.checklistItemDao().upsert(item)
    }

    suspend fun delete(item: LocalChecklistItem) {
        db.checklistItemDao().delete(item)
    }
}