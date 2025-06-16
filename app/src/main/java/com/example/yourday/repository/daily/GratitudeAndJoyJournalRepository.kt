package com.example.yourday.repository.daily

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import kotlinx.coroutines.flow.Flow

class GratitudeAndJoyJournalRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<LocalGratitudeAndJoyJournal?> {
        return db.gratitudeAndJoyJournalDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalGratitudeAndJoyJournal? {
        return db.gratitudeAndJoyJournalDao().getById(id)
    }

    suspend fun upsert(journal: LocalGratitudeAndJoyJournal) {
        db.gratitudeAndJoyJournalDao().upsert(journal)
    }
    suspend fun delete(journal: LocalGratitudeAndJoyJournal) {
        db.gratitudeAndJoyJournalDao().delete(journal)
    }
}