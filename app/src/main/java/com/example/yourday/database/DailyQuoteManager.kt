package com.example.yourday.database

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyQuoteManager(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("DailyQuotePrefs", Context.MODE_PRIVATE)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Получить сегодняшнюю цитату (если уже выбрана) или null
    fun getTodaysQuote(): Pair<Int, Int>? {
        val today = dateFormat.format(Date())
        val savedDate = sharedPref.getString("quote_date", "")
        if (savedDate != today) return null

        return Pair(
            sharedPref.getInt("quote_id", -1),
            sharedPref.getInt("photo_id", -1)
        )
    }

    // Сохранить сегодняшнюю цитату
    fun saveTodaysQuote(quoteId: Int, photoId: Int) {
        val today = dateFormat.format(Date())
        with(sharedPref.edit()) {
            putString("quote_date", today)
            putInt("quote_id", quoteId)
            putInt("photo_id", photoId)
            apply()
        }
    }

    // Очистить сохраненную цитату (для тестирования)
    fun clearSavedQuote() {
        with(sharedPref.edit()) {
            remove("quote_date")
            remove("quote_id")
            remove("photo_id")
            apply()
        }
    }
}

class MotivationalCardManager(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("MotivationalCardPrefs", Context.MODE_PRIVATE)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Сохранить индекс карточки на сегодня
    fun saveTodaysCardIndex(cardId: Int) {
        val today = dateFormat.format(Date())
        with(sharedPref.edit()) {
            putString("card_date", today)
            putInt("card_id", cardId)
            apply()
        }
    }

    // Получить сегодняшний индекс карточки (null если еще не сохранено)
    fun getTodaysCardIndex(): Int? {
        val today = dateFormat.format(Date())
        val savedDate = sharedPref.getString("card_date", "")
        if (savedDate != today) return null

        return sharedPref.getInt("card_id", -1).takeIf { it != -1 }
    }

    // Очистить сохраненные данные (для тестирования)
    fun clearSavedCard() {
        with(sharedPref.edit()) {
            remove("card_date")
            remove("card_id")
            apply()
        }
    }
}