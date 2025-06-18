package com.example.yourday.data

import com.example.yourday.model.ArticleCategory

// Объект, содержащий моковые данные категорий статей
object MockCategoryRepository {
    private val mockCategories = listOf(
        ArticleCategory(1, "Планирование и продуктивность"),
        ArticleCategory(2, "Здоровье и спорт"),
        ArticleCategory(3, "Психология и мотивация"),
        ArticleCategory(4, "Финансы"),
        ArticleCategory(5, "Саморазвитие"),
        ArticleCategory(6, "Творчество и хобби"),
        ArticleCategory(7, "Отношения и коммуникация"),
        ArticleCategory(8, "Технологии и гаджеты"),
        ArticleCategory(9, "Путешествия и образ жизни"),
        ArticleCategory(10, "Психология и мотивация"),
        ArticleCategory(11, "Специальная категория"),
    )

    // Функция для получения категории по ID
    // Возвращает ArticleCategory, если категория с таким ID найдена, иначе null
    fun getCategoryById(id: Int): ArticleCategory? {
        return mockCategories.firstOrNull { it.id == id }
    }


    // Функция для получения всех категорий
    // Возвращает список всех моковых категорий
    fun getAllCategories(): List<ArticleCategory> {
        return mockCategories
    }
}