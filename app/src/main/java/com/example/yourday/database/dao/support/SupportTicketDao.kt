package com.example.yourday.database.dao.support

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalSupportTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportTicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: LocalSupportTicket)

    @Update
    suspend fun update(ticket: LocalSupportTicket)

    @Delete
    suspend fun delete(ticket: LocalSupportTicket)

    @Query("SELECT * FROM support_tickets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: String): Flow<List<LocalSupportTicket>>

    @Query("SELECT * FROM support_tickets WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalSupportTicket>
}
