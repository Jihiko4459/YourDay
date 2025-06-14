package com.example.yourday.database.dao.social

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalFriendshipStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendshipStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: LocalFriendshipStatus)

    @Update
    suspend fun update(status: LocalFriendshipStatus)

    @Delete
    suspend fun delete(status: LocalFriendshipStatus)

    @Query("SELECT * FROM friendship_statuses")
    fun getAll(): Flow<List<LocalFriendshipStatus>>

    @Query("SELECT * FROM friendship_statuses WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalFriendshipStatus>
}
