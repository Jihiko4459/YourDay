package com.example.yourday.repository.health_and_fitness

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalWaterIntake
import com.example.yourday.model.WaterIntake
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class WaterIntakeRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getByDate(userId: String, date: String): Flow<List<LocalWaterIntake>> {
        return db.waterIntakeDao().getByDate(userId, date)
    }

    suspend fun addIntake(intake: LocalWaterIntake) {
        db.waterIntakeDao().insert(intake)
        syncIntakes()
    }

    private suspend fun syncIntakes() {
        val unsynced = db.waterIntakeDao().getUnsynced()
        unsynced.forEach { intake ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("water_intake").upsert(
                        WaterIntake(
                            id = intake.serverId ?: 0,
                            userId = intake.userId,
                            date = intake.date,
                            time = intake.time,
                            amountMl = intake.amountMl
                        )
                    )
                }

                if (result.isSuccess) {
                    db.waterIntakeDao().update(intake.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}