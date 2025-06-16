package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalItemMark
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemMarkDao {
    @Upsert
    suspend fun upsert(mark: LocalItemMark)

    @Delete
    suspend fun delete(mark: LocalItemMark)

    @Query("SELECT * FROM item_marks WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalItemMark>>

    @Query("SELECT * FROM item_marks WHERE id = :id")
    suspend fun getById(id: Int): LocalItemMark?
}