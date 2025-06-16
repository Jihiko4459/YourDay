package com.example.yourday.database.dao.motivation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.yourday.model.LocalMotivationalCard
import kotlinx.coroutines.flow.Flow

@Dao
interface MotivationalCardDao {
    @Upsert
    suspend fun upsert(card: LocalMotivationalCard)

    @Delete
    suspend fun delete(card: LocalMotivationalCard)

    @Query("SELECT * FROM motivational_cards")
    fun getAll(): Flow<List<LocalMotivationalCard>>


    @Query("SELECT * FROM motivational_cards WHERE id = :id")
    suspend fun getById(id: Int): LocalMotivationalCard?

    // Добавляем сразу много карточек
    @Upsert
    suspend fun insertAll(cards: List<LocalMotivationalCard>)

    // Удаляем все карточки
    @Query("DELETE FROM motivational_cards")
    suspend fun deleteAllReferenceCards()
}