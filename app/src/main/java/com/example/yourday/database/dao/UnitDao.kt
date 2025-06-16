package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {

    @Upsert
    suspend fun upsert(unit: LocalUnit)

    @Delete
    suspend fun delete(unit: LocalUnit)

    @Query("SELECT * FROM units WHERE id = :id")
    suspend fun getById(id: Int): LocalUnit?

    @Query("SELECT * FROM units")
    fun getAll(): Flow<List<LocalUnit>>

    // Новый метод для очистки таблицы
    @Query("DELETE FROM units")
    suspend fun deleteAll()

}