package com.example.yourday.repository.health_and_fitness

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalSteps
import com.example.yourday.model.Steps
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class StepsRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getByDate(userId: String, date: String): Flow<LocalSteps?> {
        return db.stepsDao().getByDate(userId, date)
    }

    suspend fun addSteps(steps: LocalSteps) {
        db.stepsDao().insert(steps)
        syncSteps()
    }

    private suspend fun syncSteps() {
        val unsynced = db.stepsDao().getUnsynced()
        unsynced.forEach { steps ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("steps").upsert(
                        Steps(
                            id = steps.serverId ?: 0,
                            userId = steps.userId,
                            stepsCount = steps.stepsCount,
                            date = steps.date
                        )
                    )
                }

                if (result.isSuccess) {
                    db.stepsDao().update(steps.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}