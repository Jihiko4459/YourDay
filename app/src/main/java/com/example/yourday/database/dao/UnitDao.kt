package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unit: LocalUnit)

    @Update
    suspend fun update(unit: LocalUnit)

    @Delete
    suspend fun delete(unit: LocalUnit)

    @Query("SELECT * FROM units")
    fun getAll(): Flow<List<LocalUnit>>

    @Query("SELECT * FROM units WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalUnit>
}