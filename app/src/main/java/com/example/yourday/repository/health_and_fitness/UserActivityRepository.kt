package com.example.yourday.repository.health_and_fitness

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalUserActivity
import kotlinx.coroutines.flow.Flow

class UserActivityRepository(private val db: YourDayDatabase) {
    fun getByDate(userId: String, date: String): Flow<List<LocalUserActivity>> {
        return db.userActivityDao().getByDate(userId, date)
    }

    suspend fun getById(id: Int): LocalUserActivity? {
        return db.userActivityDao().getById(id)
    }

    suspend fun upsert(activity: LocalUserActivity) {
        db.userActivityDao().upsert(activity)
    }


    suspend fun delete(activity: LocalUserActivity) {
        db.userActivityDao().delete(activity)
    }
}
