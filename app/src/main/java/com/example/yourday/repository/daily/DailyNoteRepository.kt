package com.example.yourday.repository.daily

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.DailyNote
import com.example.yourday.model.LocalDailyNote
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class DailyNoteRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getNotesByDate(userId: String, date: String): Flow<List<LocalDailyNote>> {
        return db.dailyNoteDao().getNotesByDate(userId, date)
    }

    suspend fun addNote(note: LocalDailyNote) {
        db.dailyNoteDao().insert(note)
        syncNotes()
    }

    private suspend fun syncNotes() {
        val unsynced = db.dailyNoteDao().getUnsyncedNotes()
        unsynced.forEach { note ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("daily_notes").upsert(
                        DailyNote(
                            id = note.serverId ?: 0,
                            userId = note.userId,
                            date = note.date,
                            note = note.note
                        )
                    )
                }

                if (result.isSuccess) {
                    db.dailyNoteDao().update(note.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}