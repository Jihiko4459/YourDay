package com.example.yourday.database.dao.social

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(notification: LocalNotification)

    @Update
    suspend fun update(notification: LocalNotification)

    @Delete
    suspend fun delete(notification: LocalNotification)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: String): Flow<List<LocalNotification>>

    @Query("SELECT * FROM notifications WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalNotification>
}