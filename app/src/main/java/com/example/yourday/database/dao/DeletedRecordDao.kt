package com.example.yourday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yourday.model.LocalDeletedRecord

@Dao
interface DeletedRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: LocalDeletedRecord)

    @Query("SELECT * FROM deleted_records WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalDeletedRecord>

    @Query("DELETE FROM deleted_records WHERE localId = :id")
    suspend fun deleteById(id: Int)
}