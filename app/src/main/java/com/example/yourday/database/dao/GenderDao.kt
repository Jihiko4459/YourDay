package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalGender
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gender: LocalGender)

    @Update
    suspend fun update(gender: LocalGender)

    @Delete
    suspend fun delete(gender: LocalGender)

    @Query("SELECT * FROM genders")
    fun getAll(): Flow<List<LocalGender>>

    @Query("SELECT * FROM genders WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalGender>
}