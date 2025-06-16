package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeAndJoyJournalDao {
    @Upsert
    suspend fun upsert(journal: LocalGratitudeAndJoyJournal)

    @Delete
    suspend fun delete(journal: LocalGratitudeAndJoyJournal)

    @Query("SELECT * FROM gratitude_and_joy_journals WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalGratitudeAndJoyJournal?>

    @Query("SELECT * FROM gratitude_and_joy_journals WHERE id = :id")
    suspend fun getById(id: Int): LocalGratitudeAndJoyJournal?
}