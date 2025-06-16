package com.example.yourday.viewmodel.motivation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.model.LocalDailyPhoto
import com.example.yourday.model.LocalDailyQuote
import com.example.yourday.model.LocalMotivationalCard
import com.example.yourday.repository.motivation.MotivationalCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MotivationalCardViewModel(
    application: Application,
    private val repository: MotivationalCardRepository
) : AndroidViewModel(application) {
    private val _cards = MutableStateFlow<List<LocalMotivationalCard>>(emptyList())
    val cards: StateFlow<List<LocalMotivationalCard>> = _cards

    init {
        loadAllCards()
    }

    private fun loadAllCards() {
        viewModelScope.launch {
            repository.getAll().collect { cards ->
                _cards.value = cards
            }
        }
    }

    suspend fun getCardWithDetails(cardId: Int): Pair<LocalDailyQuote?, LocalDailyPhoto?> {
        val card = _cards.value.firstOrNull { it.id == cardId }
        return Pair(
            card?.quoteId?.let { repository.getQuote(it) },
            card?.photoId?.let { repository.getPhoto(it) }
        )
    }

    private fun loadCards() {
        viewModelScope.launch {
            repository.getAll().collect { cardsList ->
                _cards.value = cardsList
            }
        }
    }

    fun upsertCard(card: LocalMotivationalCard) {
        viewModelScope.launch {
            repository.upsert(card)
        }
    }

    fun deleteCard(card: LocalMotivationalCard) {
        viewModelScope.launch {
            repository.delete(card)
        }
    }

    // Добавление ВСЕХ справочных карточек (203 шт)
    fun insertAllReferenceCards() {
        viewModelScope.launch {
            val referenceCards = listOf(
                LocalMotivationalCard(1, 138, 27),
                LocalMotivationalCard(2, 141, 27),
                LocalMotivationalCard(3, 11, 27),
                LocalMotivationalCard(4, 31, 27),
                LocalMotivationalCard(5, 191, 27),
                LocalMotivationalCard(6, 42, 34),
                LocalMotivationalCard(7, 59, 34),
                LocalMotivationalCard(8, 112, 34),
                LocalMotivationalCard(9, 20, 34),
                LocalMotivationalCard(10, 164, 34),
                LocalMotivationalCard(11, 163, 13),
                LocalMotivationalCard(12, 179, 13),
                LocalMotivationalCard(13, 99, 13),
                LocalMotivationalCard(14, 134, 13),
                LocalMotivationalCard(15, 55, 13),
                LocalMotivationalCard(16, 117, 24),
                LocalMotivationalCard(17, 71, 24),
                LocalMotivationalCard(18, 2, 24),
                LocalMotivationalCard(19, 68, 24),
                LocalMotivationalCard(20, 24, 24),
                LocalMotivationalCard(21, 196, 5),
                LocalMotivationalCard(22, 180, 5),
                LocalMotivationalCard(23, 40, 5),
                LocalMotivationalCard(24, 171, 5),
                LocalMotivationalCard(25, 27, 5),
                LocalMotivationalCard(26, 131, 6),
                LocalMotivationalCard(27, 167, 6),
                LocalMotivationalCard(28, 13, 6),
                LocalMotivationalCard(29, 135, 6),
                LocalMotivationalCard(30, 65, 6),
                LocalMotivationalCard(31, 103, 7),
                LocalMotivationalCard(32, 161, 7),
                LocalMotivationalCard(33, 30, 7),
                LocalMotivationalCard(34, 92, 7),
                LocalMotivationalCard(35, 32, 7),
                LocalMotivationalCard(36, 176, 2),
                LocalMotivationalCard(37, 16, 2),
                LocalMotivationalCard(38, 156, 2),
                LocalMotivationalCard(39, 98, 2),
                LocalMotivationalCard(40, 49, 2),
                LocalMotivationalCard(41, 36, 11),
                LocalMotivationalCard(42, 139, 11),
                LocalMotivationalCard(43, 85, 11),
                LocalMotivationalCard(44, 183, 11),
                LocalMotivationalCard(45, 77, 11),
                LocalMotivationalCard(46, 1, 9),
                LocalMotivationalCard(47, 148, 9),
                LocalMotivationalCard(48, 34, 9),
                LocalMotivationalCard(49, 29, 9),
                LocalMotivationalCard(50, 133, 9),
                LocalMotivationalCard(51, 132, 19),
                LocalMotivationalCard(52, 174, 19),
                LocalMotivationalCard(53, 38, 19),
                LocalMotivationalCard(54, 151, 19),
                LocalMotivationalCard(55, 162, 19),
                LocalMotivationalCard(56, 57, 26),
                LocalMotivationalCard(57, 88, 26),
                LocalMotivationalCard(58, 54, 26),
                LocalMotivationalCard(59, 108, 26),
                LocalMotivationalCard(60, 94, 26),
                LocalMotivationalCard(61, 106, 10),
                LocalMotivationalCard(62, 15, 10),
                LocalMotivationalCard(63, 189, 10),
                LocalMotivationalCard(64, 21, 10),
                LocalMotivationalCard(65, 73, 10),
                LocalMotivationalCard(66, 153, 23),
                LocalMotivationalCard(67, 129, 23),
                LocalMotivationalCard(68, 142, 23),
                LocalMotivationalCard(69, 175, 23),
                LocalMotivationalCard(70, 166, 23),
                LocalMotivationalCard(71, 10, 8),
                LocalMotivationalCard(72, 61, 8),
                LocalMotivationalCard(73, 121, 8),
                LocalMotivationalCard(74, 122, 8),
                LocalMotivationalCard(75, 14, 8),
                LocalMotivationalCard(76, 202, 38),
                LocalMotivationalCard(77, 120, 38),
                LocalMotivationalCard(78, 66, 38),
                LocalMotivationalCard(79, 25, 38),
                LocalMotivationalCard(80, 67, 38),
                LocalMotivationalCard(81, 79, 37),
                LocalMotivationalCard(82, 159, 37),
                LocalMotivationalCard(83, 185, 37),
                LocalMotivationalCard(84, 56, 37),
                LocalMotivationalCard(85, 43, 37),
                LocalMotivationalCard(86, 63, 12),
                LocalMotivationalCard(87, 72, 12),
                LocalMotivationalCard(88, 33, 12),
                LocalMotivationalCard(89, 126, 12),
                LocalMotivationalCard(90, 81, 12),
                LocalMotivationalCard(91, 114, 20),
                LocalMotivationalCard(92, 86, 20),
                LocalMotivationalCard(93, 160, 20),
                LocalMotivationalCard(94, 177, 20),
                LocalMotivationalCard(95, 46, 20),
                LocalMotivationalCard(96, 37, 30),
                LocalMotivationalCard(97, 145, 30),
                LocalMotivationalCard(98, 4, 30),
                LocalMotivationalCard(99, 165, 30),
                LocalMotivationalCard(100, 8, 30),
                LocalMotivationalCard(101, 6, 1),
                LocalMotivationalCard(102, 105, 1),
                LocalMotivationalCard(103, 192, 1),
                LocalMotivationalCard(104, 113, 1),
                LocalMotivationalCard(105, 158, 1),
                LocalMotivationalCard(106, 5, 25),
                LocalMotivationalCard(107, 58, 25),
                LocalMotivationalCard(108, 84, 25),
                LocalMotivationalCard(109, 100, 25),
                LocalMotivationalCard(110, 62, 25),
                LocalMotivationalCard(111, 190, 15),
                LocalMotivationalCard(112, 3, 15),
                LocalMotivationalCard(113, 123, 15),
                LocalMotivationalCard(114, 39, 15),
                LocalMotivationalCard(115, 200, 15),
                LocalMotivationalCard(116, 44, 33),
                LocalMotivationalCard(117, 173, 33),
                LocalMotivationalCard(118, 74, 33),
                LocalMotivationalCard(119, 102, 33),
                LocalMotivationalCard(120, 97, 29),
                LocalMotivationalCard(121, 80, 29),
                LocalMotivationalCard(122, 140, 29),
                LocalMotivationalCard(123, 70, 29),
                LocalMotivationalCard(124, 28, 4),
                LocalMotivationalCard(125, 170, 4),
                LocalMotivationalCard(126, 41, 4),
                LocalMotivationalCard(127, 147, 4),
                LocalMotivationalCard(128, 12, 35),
                LocalMotivationalCard(129, 76, 35),
                LocalMotivationalCard(130, 26, 35),
                LocalMotivationalCard(131, 45, 35),
                LocalMotivationalCard(132, 125, 18),
                LocalMotivationalCard(133, 169, 18),
                LocalMotivationalCard(134, 195, 18),
                LocalMotivationalCard(135, 48, 18),
                LocalMotivationalCard(136, 157, 28),
                LocalMotivationalCard(137, 107, 28),
                LocalMotivationalCard(138, 47, 28),
                LocalMotivationalCard(139, 143, 28),
                LocalMotivationalCard(140, 53, 32),
                LocalMotivationalCard(141, 87, 32),
                LocalMotivationalCard(142, 9, 32),
                LocalMotivationalCard(143, 124, 32),
                LocalMotivationalCard(144, 90, 31),
                LocalMotivationalCard(145, 178, 31),
                LocalMotivationalCard(146, 181, 31),
                LocalMotivationalCard(147, 203, 31),
                LocalMotivationalCard(148, 197, 17),
                LocalMotivationalCard(149, 23, 17),
                LocalMotivationalCard(150, 144, 17),
                LocalMotivationalCard(151, 69, 17),
                LocalMotivationalCard(152, 137, 40),
                LocalMotivationalCard(153, 154, 40),
                LocalMotivationalCard(154, 199, 40),
                LocalMotivationalCard(155, 64, 40),
                LocalMotivationalCard(156, 96, 22),
                LocalMotivationalCard(157, 146, 22),
                LocalMotivationalCard(158, 75, 22),
                LocalMotivationalCard(159, 187, 22),
                LocalMotivationalCard(160, 95, 44),
                LocalMotivationalCard(161, 82, 44),
                LocalMotivationalCard(162, 101, 44),
                LocalMotivationalCard(163, 194, 44),
                LocalMotivationalCard(164, 110, 16),
                LocalMotivationalCard(165, 104, 16),
                LocalMotivationalCard(166, 193, 16),
                LocalMotivationalCard(167, 19, 16),
                LocalMotivationalCard(168, 118, 21),
                LocalMotivationalCard(169, 128, 21),
                LocalMotivationalCard(170, 184, 21),
                LocalMotivationalCard(171, 136, 21),
                LocalMotivationalCard(172, 52, 41),
                LocalMotivationalCard(173, 188, 41),
                LocalMotivationalCard(174, 51, 41),
                LocalMotivationalCard(175, 201, 41),
                LocalMotivationalCard(176, 18, 43),
                LocalMotivationalCard(177, 150, 43),
                LocalMotivationalCard(178, 127, 43),
                LocalMotivationalCard(179, 35, 43),
                LocalMotivationalCard(180, 91, 3),
                LocalMotivationalCard(181, 111, 3),
                LocalMotivationalCard(182, 186, 3),
                LocalMotivationalCard(183, 17, 3),
                LocalMotivationalCard(184, 89, 42),
                LocalMotivationalCard(185, 109, 42),
                LocalMotivationalCard(186, 50, 42),
                LocalMotivationalCard(187, 78, 42),
                LocalMotivationalCard(188, 60, 36),
                LocalMotivationalCard(189, 168, 36),
                LocalMotivationalCard(190, 115, 36),
                LocalMotivationalCard(191, 130, 36),
                LocalMotivationalCard(192, 198, 45),
                LocalMotivationalCard(193, 119, 45),
                LocalMotivationalCard(194, 182, 45),
                LocalMotivationalCard(195, 149, 45),
                LocalMotivationalCard(196, 7, 39),
                LocalMotivationalCard(197, 155, 39),
                LocalMotivationalCard(198, 22, 39),
                LocalMotivationalCard(199, 83, 39),
                LocalMotivationalCard(200, 116, 14),
                LocalMotivationalCard(201, 152, 14),
                LocalMotivationalCard(202, 172, 14),
                LocalMotivationalCard(203, 93, 14)
            )
            repository.insertAllReferenceCards(referenceCards)
            loadCards() // Обновляем список после добавления
        }
    }

    // Удаление ВСЕХ карточек
    fun deleteAllReferenceCards() {
        viewModelScope.launch {
            repository.deleteAllReferenceCards()
            _cards.value = emptyList() // Очищаем список
        }
    }
}