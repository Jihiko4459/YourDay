package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalChecklistCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: LocalChecklistCategory)

    @Update
    suspend fun update(category: LocalChecklistCategory)

    @Delete
    suspend fun delete(category: LocalChecklistCategory)

    @Query("SELECT * FROM checklist_categories")
    fun getAll(): Flow<List<LocalChecklistCategory>>

    @Query("SELECT * FROM checklist_categories WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalChecklistCategory>
}