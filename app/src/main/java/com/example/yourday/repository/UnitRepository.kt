package com.example.yourday.repository

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalUnit
import kotlinx.coroutines.flow.Flow


class UnitRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalUnit>> {
        return db.unitDao().getAll()
    }

    suspend fun getById(id: Int): LocalUnit? {
        return db.unitDao().getById(id)
    }

    suspend fun upsert(unit: LocalUnit) {
        db.unitDao().upsert(unit)
    }

    suspend fun delete(unit: LocalUnit) {
        db.unitDao().delete(unit)
    }
}