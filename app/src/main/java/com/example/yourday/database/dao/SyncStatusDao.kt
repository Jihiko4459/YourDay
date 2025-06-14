package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.SyncStatus

@Dao
interface SyncStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: SyncStatus)

    @Update
    suspend fun update(status: SyncStatus)

    @Query("SELECT * FROM sync_status WHERE tableName = :tableName")
    suspend fun getByTable(tableName: String): SyncStatus?

    @Query("SELECT * FROM sync_status WHERE pendingChanges = 1")
    suspend fun getPendingSyncs(): List<SyncStatus>
}