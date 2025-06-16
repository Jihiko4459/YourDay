package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalDailyQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuoteDao {
    @Upsert
    suspend fun upsert(quote: LocalDailyQuote)

    @Delete
    suspend fun delete(quote: LocalDailyQuote)

    @Query("SELECT * FROM daily_quotes WHERE date = :date")
    fun getByDate(date: String): Flow<LocalDailyQuote?>


    @Query("SELECT * FROM daily_quotes WHERE id = :id")
    suspend fun getById(id: Int): LocalDailyQuote?

    // Новый метод для очистки таблицы
    @Query("DELETE FROM daily_quotes")
    suspend fun deleteAll()
}