package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalUserActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: LocalUserActivity)

    @Update
    suspend fun update(activity: LocalUserActivity)

    @Delete
    suspend fun delete(activity: LocalUserActivity)

    @Query("SELECT * FROM user_activities WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalUserActivity>>

    @Query("SELECT * FROM user_activities WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalUserActivity>
}