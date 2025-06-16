package com.example.yourday.repository.checklist

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalItemMark
import kotlinx.coroutines.flow.Flow

class ItemMarkRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalItemMark>> {
        return db.itemMarkDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalItemMark? {
        return db.itemMarkDao().getById(id)
    }

    suspend fun upsert(mark: LocalItemMark) {
        db.itemMarkDao().upsert(mark)
    }

    suspend fun delete(mark: LocalItemMark) {
        db.itemMarkDao().delete(mark)
    }
}