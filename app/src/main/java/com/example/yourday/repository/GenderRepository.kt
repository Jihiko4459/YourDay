package com.example.yourday.repository

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGender
import kotlinx.coroutines.flow.Flow

class GenderRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalGender>> {
        return db.genderDao().getAll()
    }

    suspend fun getById(id: Int): LocalGender? {
        return db.genderDao().getById(id)
    }

    suspend fun upsert(gender: LocalGender) {
        db.genderDao().upsert(gender)
    }

    suspend fun delete(gender: LocalGender) {
        db.genderDao().delete(gender)
    }

    // Добавляем все справочные гендеры
    suspend fun insertAllReferenceGenders(genders: List<LocalGender>) {
        db.genderDao().insertAll(genders)
    }

    // Удаляем все справочные гендеры
    suspend fun deleteAllReferenceGenders() {
        db.genderDao().deleteAllReferenceGenders()
    }
}
