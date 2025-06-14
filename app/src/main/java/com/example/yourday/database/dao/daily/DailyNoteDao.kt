package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalDailyNote
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyNoteDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(note: LocalDailyNote)

    @Upsert
    suspend fun update(note: LocalDailyNote)

    @Delete
    suspend fun delete(note: LocalDailyNote)

    @Query("SELECT * FROM daily_notes WHERE userId = :userId AND date = :date")
    fun getNotesByDate(userId: String, date: String): Flow<List<LocalDailyNote>>

    @Query("SELECT * FROM daily_notes WHERE isSynced = 0")
    suspend fun getUnsyncedNotes(): List<LocalDailyNote>
}