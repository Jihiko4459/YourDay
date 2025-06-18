// Пакет, содержащий все модели данных приложения
package com.example.yourday.model

// Импорт необходимых сериализаторов и зависимостей
import com.example.yourday.api.LocalDateSerializer
import com.example.yourday.api.LocalDateSerializerForInsertProfile
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Модель данных профиля пользователя
@Serializable
data class ProfileData(
    @SerialName("user_id") val userId: String,                     // ID пользователя
    @SerialName("username") val username: String,                  // Имя пользователя
    @Serializable(with = LocalDateSerializerForInsertProfile::class)
    @SerialName("birth_date") val birthDate: LocalDate,            // Дата рождения
    @SerialName("gender_id") val genderId: Int? = null,            // ID пола (опционально)
    @SerialName("avatar_url") val avatarUrl: String? = null,       // URL аватара (опционально)
    @SerialName("friendship_code") val friendshipCode: String,     // Код для добавления в друзья
    @SerialName("registration_date") val registrationDate: String, // Дата регистрации
    @SerialName("last_login") val lastLogin: String,               // Последний вход
    @SerialName("is_active") val isActive: Boolean                 // Активен ли пользователь
)

// Модель типа активности
@Serializable
data class ActivityType(
    @SerialName("id") val id: Int,                     // ID типа активности
    @SerialName("name") val name: String,              // Название
    @SerialName("calories_per_min") val caloriesPerMin: Double? = null, // Калории в минуту
    @SerialName("icon_type") val iconType: String? = null // Тип иконки
)

// Модель иконки приложения
@Serializable
data class AppIcon(
    @SerialName("id") val id: Int,                     // ID иконки
    @SerialName("name") val name: String,              // Название
    @SerialName("url") val url: String? = null,        // URL изображения
    @SerialName("is_active") val isActive: Boolean = true // Активна ли иконка
)

// Модель категории статей
@Serializable
data class ArticleCategory(
    @SerialName("id") val id: Int,                     // ID категории
    @SerialName("category_name") val categoryName: String // Название категории
)

// Модель статуса статьи
@Serializable
data class ArticleStatus(
    @SerialName("id") val id: Int,                     // ID статуса
    @SerialName("status_name") val statusName: String  // Название статуса
)

// Модель статьи
@Serializable
data class Article(
    @SerialName("id") val id: Int,                     // ID статьи
    @SerialName("title") val title: String,            // Заголовок
    @SerialName("content") val content: String?,       // Содержание
    @SerialName("article_image") val articleImage: String?, // Изображение
    @SerialName("created_at") val createdAt: String   // Дата создания
)

// Модель связи статьи с категорией
@Serializable
data class ArticleInCategory(
    @SerialName("id") val id: Int,                     // ID связи
    @SerialName("article_id") val articleId: Int?,     // ID статьи
    @SerialName("category_id") val categoryId: Int?    // ID категории
)

// Модель типа баланса (доход/расход)
@Serializable
data class BalanceType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String     // Название типа
)

// Модель замеров тела
@Serializable
data class BodyMeasurement(
    @SerialName("id") val id: Int,                     // ID замера
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата замера
    @SerialName("weight") val weight: Double?,         // Вес
    @SerialName("height") val height: Double?,         // Рост
    @SerialName("bmi") val bmi: Double?,               // ИМТ
    @SerialName("chest_circum") val chestCircum: Double?, // Обхват груди
    @SerialName("waist_circum") val waistCircum: Double?, // Обхват талии
    @SerialName("hip_circum") val hipCircum: Double?   // Обхват бедер
)

// Модель категории чеклиста
@Serializable
data class ChecklistCategory(
    @SerialName("id") val id: Int,                     // ID категории
    @SerialName("category_name") val categoryName: String, // Название
    @SerialName("icon") val icon: String? = null       // Иконка
)

// Модель элемента чеклиста
@Serializable
data class ChecklistItem(
    @SerialName("id") val id: Int,                     // ID элемента
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("category_id") val categoryId: Int?,   // ID категории
    @SerialName("item_name") val itemName: String      // Название элемента
)

// Модель ежедневной заметки
@Serializable
data class DailyNote(
    @SerialName("id") val id: Int,                     // ID заметки
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата заметки
    @SerialName("note") val note: String?              // Текст заметки
)

// Модель ежедневного фото
@Serializable
data class DailyPhoto(
    @SerialName("id") val id: Int,                     // ID фото
    @SerialName("photo_url") val photoUrl: String,      // URL фото
    @SerialName("date") val date: String               // Дата фото
)

// Модель ежедневной цитаты
@Serializable
data class DailyQuote(
    @SerialName("id") val id: Int,                     // ID цитаты
    @SerialName("quote") val quote: String,            // Текст цитаты
    @SerialName("date") val date: String,              // Дата цитаты
    @SerialName("author_quote") val authorQuote: String = "Нет автора" // Автор
)

// Модель дня недели
@Serializable
data class DayOfTheWeek(
    @SerialName("id") val id: Int,                     // ID дня
    @SerialName("day_weekly_name") val dayWeeklyName: String // Название дня
)

// Модель удаленной записи
@Serializable
data class DeletedRecord(
    @SerialName("id") val id: Int,                     // ID удаления
    @SerialName("original_id") val originalId: Int,    // ID оригинальной записи
    @SerialName("table_name") val tableName: String,   // Имя таблицы
    @SerialName("deleted_data") val deletedData: String?, // Данные удаленной записи
    @SerialName("deleted_at") val deletedAt: String   // Время удаления
)

// Модель продукта питания
@Serializable
data class FoodItem(
    @SerialName("id") val id: Int,                     // ID продукта
    @SerialName("name") val name: String,              // Название
    @SerialName("unit_id") val unitId: Int?,           // ID единицы измерения
    @SerialName("calories_per_unit") val caloriesPerUnit: Double?, // Калории на единицу
    @SerialName("protein_per_unit") val proteinPerUnit: Double?, // Белки
    @SerialName("fat_per_unit") val fatPerUnit: Double?,       // Жиры
    @SerialName("carbs_per_unit") val carbsPerUnit: Double?    // Углеводы
)

// Модель друга
@Serializable
data class Friend(
    @SerialName("id") val id: Int,                     // ID связи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("friend_id") val friendId: String,     // ID друга
    @SerialName("status_id") val statusId: Int?,       // ID статуса дружбы
    @SerialName("created_at") val createdAt: String   // Дата создания связи
)

// Модель статуса дружбы
@Serializable
data class FriendshipStatus(
    @SerialName("id") val id: Int,                     // ID статуса
    @SerialName("status_name") val statusName: String  // Название статуса
)

// Модель пола
@Serializable
data class Gender(
    @SerialName("id") val id: Int,                     // ID пола
    @SerialName("gender_name") val genderName: String  // Название пола
)

// Модель типа цели/привычки
@Serializable
data class GoalAndHabitType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String,     // Название типа
    @SerialName("icon_type") val iconType: String? = null // Иконка типа
)

// Модель статуса цели
@Serializable
data class GoalStatus(
    @SerialName("id") val id: Int,                     // ID статуса
    @SerialName("status_name") val statusName: String  // Название статуса
)

// Модель цели
@Serializable
data class Goal(
    @SerialName("id") val id: Int,                     // ID цели
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("title") val title: String,            // Название цели
    @SerialName("description") val description: String?, // Описание
    @SerialName("goal_type_id") val goalTypeId: Int?,   // ID типа цели
    @SerialName("status_id") val statusId: Int?,       // ID статуса
    @SerialName("progress_goal") val progressGoal: Double = 0.0, // Прогресс
    @SerialName("created_at") val createdAt: String,  // Дата создания
    @SerialName("achieved_at") val achievedAt: String?, // Дата достижения
    @SerialName("last_updated") val lastUpdated: String // Последнее обновление
)

// Модель записи дневника благодарности
@Serializable
data class GratitudeAndJoyJournal(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата записи
    @SerialName("gratitude") val gratitude: String?,   // За что благодарен
    @SerialName("joy") val joy: String?                // Что порадовало
)

// Модель привычки
@Serializable
data class Habit(
    @SerialName("id") val id: Int,                     // ID привычки
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("title") val title: String,            // Название привычки
    @SerialName("habit_type_id") val habitTypeId: Int?, // ID типа привычки
    @SerialName("created_at") val createdAt: String   // Дата создания
)

// Модель хобби
@Serializable
data class Hobby(
    @SerialName("id") val id: Int,                     // ID хобби
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("name") val name: String,              // Название хобби
    @SerialName("description") val description: String?, // Описание
    @SerialName("day1_id") val day1Id: Int?,          // День 1 для занятия
    @SerialName("day2_id") val day2Id: Int?,          // День 2
    @SerialName("day3_id") val day3Id: Int?           // День 3
)

// Модель идеи
@Serializable
data class Idea(
    @SerialName("id") val id: Int,                     // ID идеи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата идеи
    @SerialName("title") val title: String,            // Заголовок
    @SerialName("description") val description: String? // Описание
)

// Модель отметки выполнения элемента
@Serializable
data class ItemMark(
    @SerialName("id") val id: Int,                     // ID отметки
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("item_id") val itemId: Int,            // ID элемента
    @SerialName("date") val date: String,              // Дата отметки
    @SerialName("time") val time: String?,             // Время отметки
    @SerialName("is_taken") val isTaken: Boolean = false // Выполнено ли
)

// Модель типа приема пищи
@Serializable
data class MealType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String      // Название типа
)

// Модель лекарства
@Serializable
data class Medication(
    @SerialName("id") val id: Int,                     // ID лекарства
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("name") val name: String,              // Название
    @SerialName("unit_id") val unitId: Int?,           // ID единицы измерения
    @SerialName("description") val description: String?, // Описание
    @SerialName("dosage") val dosage: String?,         // Дозировка
    @SerialName("amount_medicine") val amountMedicine: Double?, // Количество
    @SerialName("acceptance_time") val acceptanceTime: String?, // Время приема
    @SerialName("interval") val interval: String?      // Интервал приема
)

// Модель мотивационной карточки настроения
@Serializable
data class MoodMotivationalCard(
    @SerialName("id") val id: Int,                     // ID связи
    @SerialName("card_id") val cardId: Int?,           // ID карточки
    @SerialName("mood_type_id") val moodTypeId: Int?    // ID типа настроения
)

// Модель типа настроения
@Serializable
data class MoodType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String,     // Название типа
    @SerialName("icon_type") val iconType: String? = null // Иконка типа
)

// Модель мотивационной карточки
@Serializable
data class MotivationalCard(
    @SerialName("id") val id: Int,                     // ID карточки
    @SerialName("quote_id") val quoteId: Int?,         // ID цитаты
    @SerialName("photo_id") val photoId: Int?          // ID фото
)

// Модель пользовательской мотивационной карточки
@Serializable
data class MotivationalUserCard(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("card_id") val cardId: Int,            // ID карточки
    @SerialName("date") val date: String              // Дата показа
)

// Модель типа уведомления
@Serializable
data class NotificationType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String      // Название типа
)

// Модель уведомления
@Serializable
data class Notification(
    @SerialName("id") val id: Int,                     // ID уведомления
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("initiator_id") val initiatorId: String?, // ID инициатора
    @SerialName("type_id") val typeId: Int?,           // ID типа
    @SerialName("message") val message: String?,       // Сообщение
    @SerialName("created_at") val createdAt: String,   // Дата создания
    @SerialName("is_read") val isRead: Boolean = false, // Прочитано ли
    @SerialName("additional_data") val additionalData: String? // Доп. данные
)

// Модель записи о питании
@Serializable
data class NutritionLog(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("food_id") val foodId: Int?,           // ID продукта
    @SerialName("meal_type_id") val mealTypeId: Int?,   // ID типа приема пищи
    @SerialName("quantity") val quantity: Double,      // Количество
    @SerialName("date") val date: String              // Дата записи
)

// Модель записи о шагах
@Serializable
data class Steps(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("steps_count") val stepsCount: Int,    // Количество шагов
    @SerialName("date") val date: String              // Дата записи
)

// Модель заявки в поддержку
@Serializable
data class SupportTicket(
    @SerialName("id") val id: Int,                     // ID заявки
    @SerialName("user_id") val userId: String?,        // ID пользователя
    @SerialName("email") val email: String,            // Email отправителя
    @SerialName("subject") val subject: String,        // Тема
    @SerialName("description") val description: String?, // Описание
    @SerialName("id_status") val idStatus: Int?,       // ID статуса
    @SerialName("id_priority") val idPriority: Int?,   // ID приоритета
    @SerialName("created_at") val createdAt: String,   // Дата создания
    @SerialName("updated_at") val updatedAt: String    // Дата обновления
)

// Модель зависимости задач
@Serializable
data class TaskDependency(
    @SerialName("id") val id: Int,                     // ID зависимости
    @SerialName("task_id") val taskId: Int?,           // ID задачи
    @SerialName("depends_on") val dependsOn: Int?,     // ID задачи, от которой зависит
    @SerialName("order_by") val orderBy: Int?          // Порядок выполнения
)

// Модель типа приоритета задачи
@Serializable
data class TaskPriorityType(
    @SerialName("id") val id: Int,                     // ID приоритета
    @SerialName("type_name") val typeName: String      // Название типа
)

// Модель типа задачи
@Serializable
data class TaskType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String,     // Название типа
    @SerialName("type_icon") val typeIcon: String? = null // Иконка типа
)

// Модель задачи
@Serializable
data class Task(
    @SerialName("id") val id: Int = 0,                 // ID задачи (0 для новой)
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("title") val title: String,            // Заголовок
    @SerialName("description") val description: String, // Описание
    @SerialName("task_type_id") val taskTypeId: Int?,   // ID типа задачи
    @SerialName("priority_id") val priorityId: Int?,    // ID приоритета
    @SerialName("created_at")
    @Serializable(with = LocalDateSerializer::class)
    val createdAt: String,                             // Дата создания
    @SerialName("due_date")
    @Serializable(with = LocalDateSerializer::class)
    val dueDate: String,                               // Срок выполнения
    @SerialName("is_dependent")
    val isDependent: Boolean = false,                  // Зависит ли от других
    @SerialName("is_completed")
    val isCompleted: Boolean = false,                  // Выполнена ли
    @SerialName("completed_at")
    @Serializable(with = LocalDateSerializer::class)
    val completedAt: String = ""                       // Дата выполнения
)

// Модель темы приложения
@Serializable
data class Theme(
    @SerialName("id") val id: Int,                     // ID темы
    @SerialName("theme_name") val themeName: String,   // Название темы
    @SerialName("is_default") val isDefault: Boolean = false // Тема по умолчанию
)

// Модель приоритета заявки
@Serializable
data class TicketPriority(
    @SerialName("id") val id: Int,                     // ID приоритета
    @SerialName("priority_name") val priorityName: String // Название
)

// Модель статуса заявки
@Serializable
data class TicketStatus(
    @SerialName("id") val id: Int,                     // ID статуса
    @SerialName("status_name") val statusName: String  // Название статуса
)

// Модель финансовой операции
@Serializable
data class Transaction(
    @SerialName("id") val id: Int,                     // ID операции
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("title") val title: String,            // Название
    @SerialName("description") val description: String?, // Описание
    @SerialName("balance_type_id") val balanceTypeId: Int?, // ID типа операции
    @SerialName("amount") val amount: Double,         // Сумма
    @SerialName("date") val date: String              // Дата операции
)

// Модель единицы измерения
@Serializable
data class Unit(
    @SerialName("id") val id: Int,                     // ID единицы
    @SerialName("unit_name") val unitName: String      // Название единицы
)

// Модель активности пользователя
@Serializable
data class UserActivity(
    @SerialName("id") val id: Int,                     // ID активности
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата активности
    @SerialName("activity_type_id") val activityTypeId: Int?, // ID типа
    @SerialName("duration_minutes") val durationMinutes: Int // Длительность (мин)
)

// Модель статуса статьи для пользователя
@Serializable
data class UserArticleStatus(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("article_id") val articleId: Int?,      // ID статьи
    @SerialName("status_id") val statusId: Int?,        // ID статуса
    @SerialName("status_changed_at") val statusChangedAt: String // Дата изменения
)

// Модель настроения пользователя
@Serializable
data class UserMood(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата
    @SerialName("mood_type_id") val moodTypeId: Int?,   // ID типа настроения
    @SerialName("is_shared") val isShared: Boolean = false // Поделился ли
)

// Модель настроек пользователя
@Serializable
data class UserSetting(
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("id_theme") val idTheme: Int?,         // ID темы
    @SerialName("id_icon") val idIcon: Int?,           // ID иконки
    @SerialName("created_at") val createdAt: String    // Дата создания
)

// Модель самочувствия пользователя
@Serializable
data class UserWellbeing(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата
    @SerialName("wellbeing_type_id") val wellbeingTypeId: Int? // ID типа
)

// Модель потребления воды
@Serializable
data class WaterIntake(
    @SerialName("id") val id: Int,                     // ID записи
    @SerialName("user_id") val userId: String,         // ID пользователя
    @SerialName("date") val date: String,              // Дата
    @SerialName("time") val time: String?,             // Время
    @SerialName("amount_ml") val amountMl: Double      // Количество в мл
)

// Модель типа самочувствия
@Serializable
data class WellbeingType(
    @SerialName("id") val id: Int,                     // ID типа
    @SerialName("type_name") val typeName: String,     // Название типа
    @SerialName("icon_type") val iconType: String? = null // Иконка типа
)