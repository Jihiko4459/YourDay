package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalDayOfTheWeek
import kotlinx.coroutines.flow.Flow

@Dao
interface DayOfTheWeekDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: LocalDayOfTheWeek)

    @Update
    suspend fun update(day: LocalDayOfTheWeek)

    @Delete
    suspend fun delete(day: LocalDayOfTheWeek)

    @Query("SELECT * FROM days_of_the_week")
    fun getAll(): Flow<List<LocalDayOfTheWeek>>

    @Query("SELECT * FROM days_of_the_week WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalDayOfTheWeek>
}