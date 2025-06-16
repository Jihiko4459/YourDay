package com.example.yourday.database.dao.goals_and_habits

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalHabit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Upsert
    suspend fun upsert(habit: LocalHabit)

    @Delete
    suspend fun delete(habit: LocalHabit)

    @Query("SELECT * FROM habits WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalHabit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getById(id: Int): LocalHabit?
}