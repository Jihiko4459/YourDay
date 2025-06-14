//package com.example.yourday.repository
//
//import com.example.yourday.api.SupabaseHelper
//import com.example.yourday.database.YourDayDatabase
//import com.example.yourday.model.LocalUserSettings
//import io.github.jan.supabase.postgrest.from
//import kotlinx.coroutines.flow.Flow
//
//class UserSettingsRepository(
//    private val db: YourDayDatabase,
//    private val supabaseHelper: SupabaseHelper
//) {
//    fun getSettings(userId: String): Flow<LocalUserSettings?> {
//        return db.userSettingsDao().getSettings(userId)
//    }
//
//    suspend fun updateSettings(settings: LocalUserSettings) {
//        db.userSettingsDao().update(settings)
//        syncSettings()
//    }
//
//    private suspend fun syncSettings() {
//        val unsynced = db.userSettingsDao().getUnsyncedSettings()
//        unsynced.forEach { settings ->
//            try {
//                val result = supabaseHelper.withAuth {
//                    supabaseHelper.client.from("user_settings").upsert(
//                        UserSettings(
//                            userId = settings.userId,
//                            themeId = settings.themeId,
//                            appIconId = settings.appIconId,
//                            createdAt = settings.createdAt
//                        )
//                    )
//                }
//
//                if (result.isSuccess) {
//                    db.userSettingsDao().update(settings.copy(isSynced = true))
//                }
//            } catch (e: Exception) {
//                // Error handling
//            }
//        }
//    }
//}
