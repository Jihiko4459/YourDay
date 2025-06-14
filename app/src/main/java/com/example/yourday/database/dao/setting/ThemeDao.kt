package com.example.yourday.database.dao.setting

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTheme
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(theme: LocalTheme)

    @Update
    suspend fun update(theme: LocalTheme)

    @Delete
    suspend fun delete(theme: LocalTheme)

    @Query("SELECT * FROM themes")
    fun getAll(): Flow<List<LocalTheme>>

    @Query("SELECT * FROM themes WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTheme>
}