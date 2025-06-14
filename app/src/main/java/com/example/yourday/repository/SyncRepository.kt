package com.example.yourday.repository

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.SyncStatus

// SyncRepository.kt
class SyncRepository(
    private val db: YourDayDatabase,
    private val supabaseHelper: SupabaseHelper
) {
    suspend fun syncAll() {
        // Здесь можно вызвать синхронизацию для всех репозиториев
        // или реализовать более сложную логику синхронизации
    }

    suspend fun getPendingSyncs(): List<SyncStatus> {
        return db.syncStatusDao().getPendingSyncs()
    }
}