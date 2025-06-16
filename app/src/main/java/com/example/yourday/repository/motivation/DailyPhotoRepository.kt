package com.example.yourday.repository.motivation

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDailyPhoto
import kotlinx.coroutines.flow.Flow

class DailyPhotoRepository(private val db: YourDayDatabase) {
    fun getByDate(date: String): Flow<List<LocalDailyPhoto>> {
        return db.dailyPhotoDao().getByDate(date)
    }

    suspend fun getById(id: Int): LocalDailyPhoto? {
        return db.dailyPhotoDao().getById(id)
    }

    suspend fun upsert(photo: LocalDailyPhoto) {
        db.dailyPhotoDao().upsert(photo)
    }

    suspend fun delete(photo: LocalDailyPhoto) {
        db.dailyPhotoDao().delete(photo)
    }

    // Добавляем все справочные фото
    suspend fun insertAllReferencePhotos(photos: List<LocalDailyPhoto>) {
        db.dailyPhotoDao().insertAll(photos)
    }

    // Удаляем все справочные фото
    suspend fun deleteAllReferencePhotos() {
        db.dailyPhotoDao().deleteAllReferencePhotos()
    }
}