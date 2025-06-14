package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalHobby
import kotlinx.coroutines.flow.Flow

@Dao
interface HobbyDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(hobby: LocalHobby)

    @Update
    suspend fun update(hobby: LocalHobby)

    @Delete
    suspend fun delete(hobby: LocalHobby)

    @Query("SELECT * FROM hobbies WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalHobby>>

    @Query("SELECT * FROM hobbies WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalHobby>
}