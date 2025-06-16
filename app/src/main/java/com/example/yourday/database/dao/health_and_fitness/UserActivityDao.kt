package com.example.yourday.database.dao.health_and_fitness

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalUserActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    @Upsert
    suspend fun upsert(activity: LocalUserActivity)

    @Delete
    suspend fun delete(activity: LocalUserActivity)

    @Query("SELECT * FROM user_activities WHERE id = :id")
    suspend fun getById(id: Int): LocalUserActivity?

    @Query("SELECT * FROM user_activities WHERE userId = :userId AND date = :date")
    fun getByDate(userId: String, date: String): Flow<List<LocalUserActivity>>

}