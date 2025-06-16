package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalChecklistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistItemDao {
    @Upsert
    suspend fun upsert(item: LocalChecklistItem)

    @Delete
    suspend fun delete(item: LocalChecklistItem)

    @Query("SELECT * FROM checklist_items WHERE userId = :userId AND categoryId = :categoryId")
    fun getByCategory(userId: String, categoryId: Int): Flow<List<LocalChecklistItem>>

    @Query("SELECT * FROM checklist_items WHERE id = :id")
    suspend fun getById(id: Int): LocalChecklistItem?
}