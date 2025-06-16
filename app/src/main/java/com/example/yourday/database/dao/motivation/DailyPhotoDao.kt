package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalDailyPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyPhotoDao {

    @Upsert
    suspend fun upsert(photo: LocalDailyPhoto)

    @Delete
    suspend fun delete(photo: LocalDailyPhoto)

    @Query("SELECT * FROM daily_photos WHERE date = :date")
    fun getByDate(date: String): Flow<List<LocalDailyPhoto>>

    @Query("SELECT * FROM daily_photos WHERE id = :id")
    suspend fun getById(id: Int): LocalDailyPhoto?

    // Вставка списка фото
    @Upsert
    suspend fun insertAll(photos: List<LocalDailyPhoto>)

    // Удаление всех справочных фото (можно добавить условие, если нужно)
    @Query("DELETE FROM daily_photos")
    suspend fun deleteAllReferencePhotos()

}
