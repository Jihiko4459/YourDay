package com.example.yourday.database.dao.social

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalFriend
import kotlinx.coroutines.flow.Flow

// Friends
@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friend: LocalFriend)

    @Update
    suspend fun update(friend: LocalFriend)

    @Delete
    suspend fun delete(friend: LocalFriend)

    @Query("SELECT * FROM friends WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalFriend>>

    @Query("SELECT * FROM friends WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalFriend>
}