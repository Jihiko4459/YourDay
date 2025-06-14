package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalDailyPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: LocalDailyPhoto)

    @Update
    suspend fun update(photo: LocalDailyPhoto)

    @Delete
    suspend fun delete(photo: LocalDailyPhoto)

    @Query("SELECT * FROM daily_photos WHERE date = :date")
    fun getByDate(date: String): Flow<List<LocalDailyPhoto>>

    @Query("SELECT * FROM daily_photos WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalDailyPhoto>
}
