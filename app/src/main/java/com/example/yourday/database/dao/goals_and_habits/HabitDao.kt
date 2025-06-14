package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalHabit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(habit: LocalHabit)

    @Update
    suspend fun update(habit: LocalHabit)

    @Delete
    suspend fun delete(habit: LocalHabit)

    @Query("SELECT * FROM habits WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalHabit>>

    @Query("SELECT * FROM habits WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalHabit>
}