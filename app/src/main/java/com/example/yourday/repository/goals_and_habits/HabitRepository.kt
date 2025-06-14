package com.example.yourday.repository.goals_and_habits

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.Habit
import com.example.yourday.model.LocalHabit
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getByUser(userId: String): Flow<List<LocalHabit>> {
        return db.habitDao().getByUser(userId)
    }

    suspend fun addHabit(habit: LocalHabit) {
        db.habitDao().insert(habit)
        syncHabits()
    }

    private suspend fun syncHabits() {
        val unsynced = db.habitDao().getUnsynced()
        unsynced.forEach { habit ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("habits").upsert(
                        Habit(
                            id = habit.serverId ?: 0,
                            userId = habit.userId,
                            title = habit.title,
                            habitTypeId = habit.habitTypeId,
                            createdAt = habit.createdAt
                        )
                    )
                }

                if (result.isSuccess) {
                    db.habitDao().update(habit.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}