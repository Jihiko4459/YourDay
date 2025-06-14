package com.example.yourday.database.dao.setting

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalUserSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(settings: LocalUserSettings)

    @Upsert
    suspend fun update(settings: LocalUserSettings)

    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    fun getSettings(userId: String): Flow<LocalUserSettings?>

    @Query("SELECT * FROM user_settings WHERE isSynced = 0")
    suspend fun getUnsyncedSettings(): List<LocalUserSettings>
}