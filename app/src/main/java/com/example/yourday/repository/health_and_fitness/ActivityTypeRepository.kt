package com.example.yourday.repository.health_and_fitness

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.ActivityType
import com.example.yourday.model.LocalActivityType
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class ActivityTypeRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getAll(): Flow<List<LocalActivityType>> {
        return db.activityTypeDao().getAll()
    }

    suspend fun insert(type: LocalActivityType) {
        db.activityTypeDao().insert(type)
        syncTypes()
    }

    private suspend fun syncTypes() {
        val unsynced = db.activityTypeDao().getUnsynced()
        unsynced.forEach { type ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("activity_types").upsert(
                        ActivityType(
                            id = type.serverId ?: 0,
                            name = type.name,
                            caloriesPerMin = type.caloriesPerMin,
                            iconType = type.iconType
                        )
                    )
                }

                if (result.isSuccess) {
                    db.activityTypeDao().update(type.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}