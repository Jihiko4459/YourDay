package com.example.yourday.database.dao.finance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: LocalTransaction)

    @Update
    suspend fun update(transaction: LocalTransaction)

    @Delete
    suspend fun delete(transaction: LocalTransaction)

    @Query("SELECT * FROM transactions WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalTransaction>>

    @Query("SELECT * FROM transactions WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTransaction>
}