package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalItemMark
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemMarkDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(mark: LocalItemMark)

    @Update
    suspend fun update(mark: LocalItemMark)

    @Delete
    suspend fun delete(mark: LocalItemMark)

    @Query("SELECT * FROM item_marks WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalItemMark>>

    @Query("SELECT * FROM item_marks WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalItemMark>
}