package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalGender
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {
    @Upsert
    suspend fun upsert(gender: LocalGender)

    @Delete
    suspend fun delete(gender: LocalGender)

    @Query("SELECT * FROM genders WHERE id = :id")
    suspend fun getById(id: Int): LocalGender?

    @Query("SELECT * FROM genders")
    fun getAll(): Flow<List<LocalGender>>

    // Вставка списка полов
    @Upsert
    suspend fun insertAll(genders: List<LocalGender>)

    // Удаление всех справочных полов
    @Query("DELETE FROM genders")
    suspend fun deleteAllReferenceGenders()

}