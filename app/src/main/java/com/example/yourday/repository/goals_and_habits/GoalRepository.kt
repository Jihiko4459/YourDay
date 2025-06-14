package com.example.yourday.repository.goals_and_habits

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.Goal
import com.example.yourday.model.LocalGoal
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class GoalRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getByUser(userId: String): Flow<List<LocalGoal>> {
        return db.goalDao().getByUser(userId)
    }

    suspend fun addGoal(goal: LocalGoal) {
        db.goalDao().insert(goal)
        syncGoals()
    }

    private suspend fun syncGoals() {
        val unsynced = db.goalDao().getUnsynced()
        unsynced.forEach { goal ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("goals").upsert(
                        Goal(
                            id = goal.serverId ?: 0,
                            userId = goal.userId,
                            title = goal.title,
                            description = goal.description,
                            goalTypeId = goal.goalTypeId,
                            statusId = goal.statusId,
                            progressGoal = goal.progressGoal,
                            createdAt = goal.createdAt,
                            achievedAt = goal.achievedAt,
                            lastUpdated = goal.lastUpdated
                        )
                    )
                }

                if (result.isSuccess) {
                    db.goalDao().update(goal.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}