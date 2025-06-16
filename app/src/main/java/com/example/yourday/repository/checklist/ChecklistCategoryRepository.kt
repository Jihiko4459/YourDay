package com.example.yourday.repository.checklist

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalChecklistCategory
import kotlinx.coroutines.flow.Flow

class ChecklistCategoryRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalChecklistCategory>> {
        return db.checklistCategoryDao().getAll()
    }

    suspend fun getById(id: Int): LocalChecklistCategory? {
        return db.checklistCategoryDao().getById(id)
    }


    suspend fun upsert(category: LocalChecklistCategory) {
        db.checklistCategoryDao().upsert(category)
    }

    suspend fun delete(category: LocalChecklistCategory) {
        db.checklistCategoryDao().delete(category)
    }

    // Новый метод для очистки всех данных
    suspend fun deleteAll() {
        db.checklistCategoryDao().deleteAll()
    }
}