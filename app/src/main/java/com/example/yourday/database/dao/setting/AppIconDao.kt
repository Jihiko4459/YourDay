package com.example.yourday.database.dao.setting

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalAppIcon
import kotlinx.coroutines.flow.Flow

// App Icons
@Dao
interface AppIconDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(icon: LocalAppIcon)

    @Upsert
    suspend fun update(icon: LocalAppIcon)

    @Delete
    suspend fun delete(icon: LocalAppIcon)

    @Query("SELECT * FROM app_icons WHERE isActive = 1")
    fun getActiveIcons(): Flow<List<LocalAppIcon>>

    @Query("SELECT * FROM app_icons WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalAppIcon>
}