package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalChecklistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LocalChecklistItem)

    @Update
    suspend fun update(item: LocalChecklistItem)

    @Delete
    suspend fun delete(item: LocalChecklistItem)

    @Query("SELECT * FROM checklist_items WHERE userId = :userId AND categoryId = :categoryId")
    fun getByCategory(userId: String, categoryId: Int): Flow<List<LocalChecklistItem>>

    @Query("SELECT * FROM checklist_items WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalChecklistItem>
}