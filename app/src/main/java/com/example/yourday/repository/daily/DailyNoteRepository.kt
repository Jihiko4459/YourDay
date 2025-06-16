package com.example.yourday.repository.daily

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDailyNote
import kotlinx.coroutines.flow.Flow

class DailyNoteRepository(private val db: YourDayDatabase) {
    fun getNotesByDate(userId: String, date: String): Flow<List<LocalDailyNote>> {
        return db.dailyNoteDao().getNotesByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalDailyNote? {
        return db.dailyNoteDao().getById(id)
    }

    suspend fun upsert(note: LocalDailyNote) {
        db.dailyNoteDao().upsert(note)
    }

    suspend fun delete(note: LocalDailyNote) {
        db.dailyNoteDao().delete(note)
    }
}