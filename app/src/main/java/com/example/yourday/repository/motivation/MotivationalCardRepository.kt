package com.example.yourday.repository.motivation

import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalDailyPhoto
import com.example.yourday.model.LocalDailyQuote
import com.example.yourday.model.LocalMotivationalCard
import kotlinx.coroutines.flow.Flow

class MotivationalCardRepository(private val db: YourDayDatabase) {
    fun getAll(): Flow<List<LocalMotivationalCard>> {
        return db.motivationalCardDao().getAll()
    }

    suspend fun getQuote(quoteId: Int): LocalDailyQuote? {
        return db.dailyQuoteDao().getById(quoteId)
    }

    suspend fun getPhoto(photoId: Int): LocalDailyPhoto? {
        return db.dailyPhotoDao().getById(photoId)
    }

    suspend fun getById(id: Int): LocalMotivationalCard? {
        return db.motivationalCardDao().getById(id)
    }

    suspend fun upsert(card: LocalMotivationalCard) {
        db.motivationalCardDao().upsert(card)
    }


    suspend fun delete(card: LocalMotivationalCard) {
        db.motivationalCardDao().delete(card)
    }

    // Добавляем ВСЕ справочные карточки (203 штуки)
    suspend fun insertAllReferenceCards(cards: List<LocalMotivationalCard>) {
        db.motivationalCardDao().insertAll(cards)
    }

    // Удаляем ВСЕ справочные карточки
    suspend fun deleteAllReferenceCards() {
        db.motivationalCardDao().deleteAllReferenceCards()
    }
}