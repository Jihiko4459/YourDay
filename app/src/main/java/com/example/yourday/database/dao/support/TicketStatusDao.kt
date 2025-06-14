package com.example.yourday.database.dao.support

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTicketStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: LocalTicketStatus)

    @Update
    suspend fun update(status: LocalTicketStatus)

    @Delete
    suspend fun delete(status: LocalTicketStatus)

    @Query("SELECT * FROM ticket_statuses")
    fun getAll(): Flow<List<LocalTicketStatus>>

    @Query("SELECT * FROM ticket_statuses WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTicketStatus>
}