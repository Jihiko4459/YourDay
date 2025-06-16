package com.example.yourday.repository.daily

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalHobby
import kotlinx.coroutines.flow.Flow

class HobbyRepository(private val db: YourDayDatabase) {
    fun getByUser(userId: String): Flow<List<LocalHobby>> {
        return db.hobbyDao().getByUser(userId)
    }

    suspend fun getById(id: Int): LocalHobby? {
        return db.hobbyDao().getById(id)
    }

    suspend fun upsert(hobby: LocalHobby) {
        db.hobbyDao().upsert(hobby)
    }

    suspend fun delete(hobby: LocalHobby) {
        db.hobbyDao().delete(hobby)
    }
}