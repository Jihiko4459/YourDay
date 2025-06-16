package com.example.yourday.repository.finance

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTransaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalTransaction>> {
        return db.transactionDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalTransaction? {
        return db.transactionDao().getById(id)
    }

    suspend fun upsert(transaction: LocalTransaction) {
        db.transactionDao().upsert(transaction)
    }
    suspend fun delete(transaction: LocalTransaction) {
        db.transactionDao().delete(transaction)
    }
}