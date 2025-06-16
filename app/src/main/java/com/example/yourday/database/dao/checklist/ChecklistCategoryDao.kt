package com.example.yourday.database.dao.checklist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalChecklistCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistCategoryDao {
    @Upsert
    suspend fun upsert(category: LocalChecklistCategory)

    @Delete
    suspend fun delete(category: LocalChecklistCategory)

    @Query("SELECT * FROM checklist_categories")
    fun getAll(): Flow<List<LocalChecklistCategory>>

    @Query("SELECT * FROM checklist_categories WHERE id = :id")
    suspend fun getById(id: Int): LocalChecklistCategory?


    // Новый метод для очистки таблицы
    @Query("DELETE FROM checklist_categories")
    suspend fun deleteAll()

}