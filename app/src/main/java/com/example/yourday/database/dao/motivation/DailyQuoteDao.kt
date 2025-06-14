package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalDailyQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: LocalDailyQuote)

    @Update
    suspend fun update(quote: LocalDailyQuote)

    @Delete
    suspend fun delete(quote: LocalDailyQuote)

    @Query("SELECT * FROM daily_quotes WHERE date = :date")
    fun getByDate(date: String): Flow<LocalDailyQuote?>

    @Query("SELECT * FROM daily_quotes WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalDailyQuote>
}