package com.example.yourday.database.dao.social

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalNotificationType
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationTypeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(type: LocalNotificationType)

    @Update
    suspend fun update(type: LocalNotificationType)

    @Delete
    suspend fun delete(type: LocalNotificationType)

    @Query("SELECT * FROM notification_types")
    fun getAll(): Flow<List<LocalNotificationType>>

    @Query("SELECT * FROM notification_types WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalNotificationType>
}