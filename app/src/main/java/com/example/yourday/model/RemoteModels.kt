package com.example.yourday.model

import com.example.yourday.api.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Event(
    val id: String,
    val title: String,
    val date: String, // Формат "YYYY-MM-DD"
    val description: String? = null
)

@Serializable
data class ProfileData(
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("birth_date") val birthDate: LocalDate,
    @SerialName("gender_id") val genderId: Int? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("friendship_code") val friendshipCode: String,
    @SerialName("registration_date") val registrationDate: String,
    @SerialName("last_login") val lastLogin: String,
    @SerialName("is_active") val isActive: Boolean
)

data class CalendarEvent(
    val id: Int,
    val date: String, // Format: "YYYY-MM-DD"
    val title: String,
    val description: String?,
    val type: String // "daily" or "health"
)

// Объединенная модель данных для DailyScreen
data class DailyScreenData(
    val date: String, // Формат "yyyy-MM-dd"
    val tasks: List<Task>,
    val gratitudeJournal: List<GratitudeAndJoyJournal>,
    val notes: List<DailyNote>,
    val goals: List<Goal>,
    val ideas: List<Idea>,
    val finances: List<Transaction>
)
// Update HealthData model to match UI
@Serializable
data class HealthData(
    val steps: List<Steps>,
    val water: List<WaterIntake>,
    val nutrition: List<NutritionLog>,
    val activity: List<UserActivity>,
    val measurements: List<BodyMeasurement>
)


// Activity Types
@Serializable
data class ActivityType(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("calories_per_min") val caloriesPerMin: Double? = null,
    @SerialName("icon_type") val iconType: String? = null
)

// App Icons
@Serializable
data class AppIcon(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

// Article Categories
@Serializable
data class ArticleCategory(
    @SerialName("id") val id: Int,
    @SerialName("category_name") val categoryName: String
)

// Article Statuses
@Serializable
data class ArticleStatus(
    @SerialName("id") val id: Int,
    @SerialName("status_name") val statusName: String
)

// Articles
@Serializable
data class Article(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String?,
    @SerialName("article_image") val articleImage: String?,
    @SerialName("created_at") val createdAt: String
)

// Articles in Categories
@Serializable
data class ArticleInCategory(
    @SerialName("id") val id: Int,
    @SerialName("article_id") val articleId: Int?,
    @SerialName("category_id") val categoryId: Int?
)

// Balance Types
@Serializable
data class BalanceType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

// Body Measurements
@Serializable
data class BodyMeasurement(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("weight") val weight: Double?,
    @SerialName("height") val height: Double?,
    @SerialName("bmi") val bmi: Double?,
    @SerialName("chest_circum") val chestCircum: Double?,
    @SerialName("waist_circum") val waistCircum: Double?,
    @SerialName("hip_circum") val hipCircum: Double?
)

// Checklist Categories
@Serializable
data class ChecklistCategory(
    @SerialName("id") val id: Int,
    @SerialName("category_name") val categoryName: String,
    @SerialName("icon") val icon: String? = null
)

// Checklist Items
@Serializable
data class ChecklistItem(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("category_id") val categoryId: Int?,
    @SerialName("item_name") val itemName: String
)

// Daily Notes
@Serializable
data class DailyNote(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("note") val note: String?
)

// Daily Photos
@Serializable
data class DailyPhoto(
    @SerialName("id") val id: Int,
    @SerialName("photo_url") val photoUrl: String,
    @SerialName("date") val date: String
)

// Daily Quotes
@Serializable
data class DailyQuote(
    @SerialName("id") val id: Int,
    @SerialName("quote") val quote: String,
    @SerialName("date") val date: String,
    @SerialName("author_quote") val authorQuote: String = "Нет автора"
)

// Days of the Week
@Serializable
data class DayOfTheWeek(
    @SerialName("id") val id: Int,
    @SerialName("day_weekly_name") val dayWeeklyName: String
)

// Deleted Records
@Serializable
data class DeletedRecord(
    @SerialName("id") val id: Int,
    @SerialName("original_id") val originalId: Int,
    @SerialName("table_name") val tableName: String,
    @SerialName("deleted_data") val deletedData: String?,
    @SerialName("deleted_at") val deletedAt: String
)

// Food Items
@Serializable
data class FoodItem(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("unit_id") val unitId: Int?,
    @SerialName("calories_per_unit") val caloriesPerUnit: Double?,
    @SerialName("protein_per_unit") val proteinPerUnit: Double?,
    @SerialName("fat_per_unit") val fatPerUnit: Double?,
    @SerialName("carbs_per_unit") val carbsPerUnit: Double?
)


// Genders
@Serializable
data class Gender(
    @SerialName("id") val id: Int,
    @SerialName("gender_name") val genderName: String
)

// Goal and Habit Types
@Serializable
data class GoalAndHabitType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("icon_type") val iconType: String? = null
)

// Goal Statuses
@Serializable
data class GoalStatus(
    @SerialName("id") val id: Int,
    @SerialName("status_name") val statusName: String
)

// Goals
@Serializable
data class Goal(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("goal_type_id") val goalTypeId: Int?,
    @SerialName("status_id") val statusId: Int?,
    @SerialName("progress_goal") val progressGoal: Double = 0.0,
    @SerialName("created_at") val createdAt: String,
    @SerialName("achieved_at") val achievedAt: String?,
    @SerialName("last_updated") val lastUpdated: String
)

// Gratitude and Joy Journals
@Serializable
data class GratitudeAndJoyJournal(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("gratitude") val gratitude: String?,
    @SerialName("joy") val joy: String?
)

// Habits
@Serializable
data class Habit(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("habit_type_id") val habitTypeId: Int?,
    @SerialName("created_at") val createdAt: String
)

// Hobbies
@Serializable
data class Hobby(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String?,
    @SerialName("day1_id") val day1Id: Int?,
    @SerialName("day2_id") val day2Id: Int?,
    @SerialName("day3_id") val day3Id: Int?
)

// Ideas
@Serializable
data class Idea(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?
)

// Item Marks
@Serializable
data class ItemMark(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("item_id") val itemId: Int,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String?,
    @SerialName("is_taken") val isTaken: Boolean = false
)

// Meal Types
@Serializable
data class MealType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

// Medications
@Serializable
data class Medication(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("unit_id") val unitId: Int?,
    @SerialName("description") val description: String?,
    @SerialName("dosage") val dosage: String?,
    @SerialName("amount_medicine") val amountMedicine: Double?,
    @SerialName("acceptance_time") val acceptanceTime: String?,
    @SerialName("interval") val interval: String?
)

// Motivational Cards
@Serializable
data class MotivationalCard(
    @SerialName("id") val id: Int,
    @SerialName("quote_id") val quoteId: Int?,
    @SerialName("photo_id") val photoId: Int?
)


// Nutrition Log
@Serializable
data class NutritionLog(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("food_id") val foodId: Int?,
    @SerialName("meal_type_id") val mealTypeId: Int?,
    @SerialName("quantity") val quantity: Double,
    @SerialName("date") val date: String
)

// Steps
@Serializable
data class Steps(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("steps_count") val stepsCount: Int,
    @SerialName("date") val date: String
)


// Task Dependencies
@Serializable
data class TaskDependency(
    @SerialName("id") val id: Int,
    @SerialName("task_id") val taskId: Int?,
    @SerialName("depends_on") val dependsOn: Int?,
    @SerialName("order_by") val orderBy: Int?
)

// Task Priority Types
@Serializable
data class TaskPriorityType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

// Task Types
@Serializable
data class TaskType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("type_icon") val typeIcon: String? = null
)

// Tasks
@Serializable
data class Task(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("task_type_id") val taskTypeId: Int?,
    @SerialName("priority_id") val priorityId: Int?,
    @SerialName("created_at") val createdAt: String,
    @SerialName("due_date") val dueDate: String?,
    @SerialName("is_dependent") val isDependent: Boolean = false,
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("completed_at") val completedAt: String?
)

// Themes
@Serializable
data class Theme(
    @SerialName("id") val id: Int,
    @SerialName("theme_name") val themeName: String,
    @SerialName("is_default") val isDefault: Boolean = false
)


// Transactions
@Serializable
data class Transaction(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    @SerialName("balance_type_id") val balanceTypeId: Int?,
    @SerialName("amount") val amount: Double,
    @SerialName("date") val date: String
)

// Units
@Serializable
data class Unit(
    @SerialName("id") val id: Int,
    @SerialName("unit_name") val unitName: String
)

// User Activity
@Serializable
data class UserActivity(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("activity_type_id") val activityTypeId: Int?,
    @SerialName("duration_minutes") val durationMinutes: Int
)

// User Article Statuses
@Serializable
data class UserArticleStatus(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("article_id") val articleId: Int?,
    @SerialName("status_id") val statusId: Int?,
    @SerialName("status_changed_at") val statusChangedAt: String
)


// User Settings
@Serializable
data class UserSettings(
    @SerialName("user_id") val userId: String,
    @SerialName("theme_id") val themeId: Int? = null,
    @SerialName("app_icon_id") val appIconId: Int? = null,
    @SerialName("created_at") val createdAt: String
)


// Water Intake
@Serializable
data class WaterIntake(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String?,
    @SerialName("amount_ml") val amountMl: Double
)



