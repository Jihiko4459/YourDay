package com.example.yourday.repository.daily

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalIdea
import kotlinx.coroutines.flow.Flow

class IdeaRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalIdea>> {
        return db.ideaDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalIdea? {
        return db.ideaDao().getById(id)
    }

    suspend fun upsert(idea: LocalIdea) {
        db.ideaDao().upsert(idea)
    }


    suspend fun delete(idea: LocalIdea) {
        db.ideaDao().delete(idea)
    }
}