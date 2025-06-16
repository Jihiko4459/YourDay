package com.example.yourday.database.dao.daily

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalHobby
import kotlinx.coroutines.flow.Flow

@Dao
interface HobbyDao {
    @Upsert
    suspend fun upsert(hobby: LocalHobby)

    @Delete
    suspend fun delete(hobby: LocalHobby)

    @Query("SELECT * FROM hobbies WHERE userId = :userId")
    fun getByUser(userId: String): Flow<List<LocalHobby>>


    @Query("SELECT * FROM hobbies WHERE id = :id")
    suspend fun getById(id: Int): LocalHobby?
}