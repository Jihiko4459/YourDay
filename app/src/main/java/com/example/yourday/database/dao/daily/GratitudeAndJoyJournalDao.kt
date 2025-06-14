package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import kotlinx.coroutines.flow.Flow

@Dao
interface GratitudeAndJoyJournalDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(journal: LocalGratitudeAndJoyJournal)

    @Update
    suspend fun update(journal: LocalGratitudeAndJoyJournal)

    @Delete
    suspend fun delete(journal: LocalGratitudeAndJoyJournal)

    @Query("SELECT * FROM gratitude_and_joy_journals WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<LocalGratitudeAndJoyJournal?>

    @Query("SELECT * FROM gratitude_and_joy_journals WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalGratitudeAndJoyJournal>
}