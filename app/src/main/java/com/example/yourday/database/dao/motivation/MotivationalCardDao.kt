package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yourday.model.LocalMotivationalCard
import kotlinx.coroutines.flow.Flow

@Dao
interface MotivationalCardDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(card: LocalMotivationalCard)

    @Update
    suspend fun update(card: LocalMotivationalCard)

    @Delete
    suspend fun delete(card: LocalMotivationalCard)

    @Query("SELECT * FROM motivational_cards")
    fun getAll(): Flow<List<LocalMotivationalCard>>

    @Query("SELECT * FROM motivational_cards WHERE isSynced = 0")
    suspend fun getUnsynced(): List<LocalMotivationalCard>
}