package com.example.yourday.database.dao.finance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsert(transaction: LocalTransaction)

    @Delete
    suspend fun delete(transaction: LocalTransaction)

    @Query("SELECT * FROM transactions WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalTransaction>>


    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Int): LocalTransaction?
}