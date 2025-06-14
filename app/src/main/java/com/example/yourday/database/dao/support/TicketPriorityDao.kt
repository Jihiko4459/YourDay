package com.example.yourday.database.dao.support

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalTicketPriority
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketPriorityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(priority: LocalTicketPriority)

    @Update
    suspend fun update(priority: LocalTicketPriority)

    @Delete
    suspend fun delete(priority: LocalTicketPriority)

    @Query("SELECT * FROM ticket_priorities")
    fun getAll(): Flow<List<LocalTicketPriority>>

    @Query("SELECT * FROM ticket_priorities WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalTicketPriority>
}
