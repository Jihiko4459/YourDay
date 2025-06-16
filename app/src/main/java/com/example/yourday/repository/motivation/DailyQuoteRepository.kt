package com.example.yourday.repository.motivation

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDailyQuote
import kotlinx.coroutines.flow.Flow

class DailyQuoteRepository(private val db: YourDayDatabase) {
    fun getByDate(date: String): Flow<LocalDailyQuote?> {
        return db.dailyQuoteDao().getByDate(date)
    }

    suspend fun getById(id: Int): LocalDailyQuote? {
        return db.dailyQuoteDao().getById(id)
    }

    suspend fun upsert(quote: LocalDailyQuote) {
        db.dailyQuoteDao().upsert(quote)
    }

    suspend fun delete(quote: LocalDailyQuote) {
        db.dailyQuoteDao().delete(quote)
    }

    // Новый метод для очистки всех данных
    suspend fun deleteAll() {
        db.dailyQuoteDao().deleteAll()
    }
}