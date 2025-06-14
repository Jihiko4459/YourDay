package com.example.yourday.repository.settings

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.AppIcon
import com.example.yourday.model.LocalAppIcon
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class AppIconRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    fun getActiveIcons(): Flow<List<LocalAppIcon>> {
        return db.appIconDao().getActiveIcons()
    }

    suspend fun insert(icon: LocalAppIcon) {
        db.appIconDao().insert(icon)
        syncIcons()
    }

    private suspend fun syncIcons() {
        val unsynced = db.appIconDao().getUnsynced()
        unsynced.forEach { icon ->
            try {
                val result = supabaseHelper.withAuth {
                    supabaseHelper.client.from("app_icons").upsert(
                        AppIcon(
                            id = icon.serverId ?: 0,
                            name = icon.name,
                            url = icon.url,
                            isActive = icon.isActive
                        )
                    )
                }
                if (result.isSuccess) {
                    db.appIconDao().update(icon.copy(isSynced = true))
                }
            } catch (e: Exception) { /* Handle error */ }
        }
    }
}