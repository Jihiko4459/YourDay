package com.example.yourday.models

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
    val tasks: DailyTasks,
    val gratitudeJournal: GratitudeJournal,
    val notes: DailyNotes,
    val goals: DailyGoals,
    val ideas: DailyIdeas,
    val finances: DailyFinances
)

// Модели для каждого компонента
data class DailyTasks(
    val count: Int,
    val items: List<Task>,
    val recommendation: String,
    val emergency: String
)

data class GratitudeJournal(
    val gratitudeItems: List<String>,
    val joyItems: List<String>
)

data class DailyNotes(
    val items: List<DailyNote>
)

data class DailyGoals(
    val activeGoals: List<GoalProgress>
)

data class GoalProgress(
    val goal: Goal,
    val progress: Double
)

data class DailyIdeas(
    val items: List<Idea>
)

data class DailyFinances(
    val balance: Double,
    val income: Double,
    val expenses: Double,
    val lastTransaction: Transaction?
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


// Tasks module
@Serializable
data class TaskDependency(
    @SerialName("id") val id: Int,
    @SerialName("task_id") val taskId: Int,
    @SerialName("depends_on") val dependsOn: Int,
    @SerialName("order_by") val orderBy: Int
)

@Serializable
data class TaskPriorityType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

@Serializable
data class TaskType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("type_icon") val typeIcon: String? = null
)

@Serializable
data class Task(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("task_type_id") val taskTypeId: Int? = null,
    @SerialName("priority_id") val priorityId: Int? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("due_date") val dueDate: String? = null,
    @SerialName("is_dependent") val isDependent: Boolean = false,
    @SerialName("is_completed") val isCompleted: Boolean = false,
    @SerialName("completed_at") val completedAt: String? = null
)

// Checklist module
@Serializable
data class ChecklistCategory(
    @SerialName("id") val id: Int,
    @SerialName("category_name") val categoryName: String,
    @SerialName("icon") val icon: String? = null
)

@Serializable
data class ChecklistItem(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("category_id") val categoryId: Int? = null,
    @SerialName("item_name") val itemName: String
)

@Serializable
data class ItemMark(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("item_id") val itemId: Int,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String? = null,
    @SerialName("is_taken") val isTaken: Boolean = false
)

// Daily module
@Serializable
data class DailyNote(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("note") val note: String? = null
)

@Serializable
data class GratitudeAndJoyJournal(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("gratitude") val gratitude: String? = null,
    @SerialName("joy") val joy: String? = null
)

@Serializable
data class Hobby(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("day1_id") val day1Id: Int? = null,
    @SerialName("day2_id") val day2Id: Int? = null,
    @SerialName("day3_id") val day3Id: Int? = null
)

@Serializable
data class Idea(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null
)

@Serializable
data class MoodType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("icon_type") val iconType: String? = null
)

@Serializable
data class UserMood(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("mood_type_id") val moodTypeId: Int? = null,
    @SerialName("is_shared") val isShared: Boolean = false
)

@Serializable
data class WellbeingType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("icon_type") val iconType: String? = null
)

@Serializable
data class UserWellbeing(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("wellbeing_type_id") val wellbeingTypeId: Int? = null
)

// Finance module
@Serializable
data class BalanceType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

@Serializable
data class Transaction(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("balance_type_id") val balanceTypeId: Int? = null,
    @SerialName("amount") val amount: Double,
    @SerialName("date") val date: String
)

// Goals and Habits module
@Serializable
data class GoalAndHabitType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String,
    @SerialName("icon_type") val iconType: String? = null
)

@Serializable
data class GoalStatus(
    @SerialName("id") val id: Int,
    @SerialName("status_name") val statusName: String
)

@Serializable
data class Goal(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("goal_type_id") val goalTypeId: Int? = null,
    @SerialName("status_id") val statusId: Int? = null,
    @SerialName("progress_goal") val progressGoal: Double = 0.0,
    @SerialName("created_at") val createdAt: String,
    @SerialName("achieved_at") val achievedAt: String? = null,
    @SerialName("last_updated") val lastUpdated: String
)

@Serializable
data class Habit(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("title") val title: String,
    @SerialName("habit_type_id") val habitTypeId: Int? = null,
    @SerialName("created_at") val createdAt: String
)

// Health and Fitness module
@Serializable
data class ActivityType(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("calories_per_min") val caloriesPerMin: Double? = null,
    @SerialName("icon_type") val iconType: String? = null
)

@Serializable
data class BodyMeasurement(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("weight") val weight: Double? = null,
    @SerialName("height") val height: Double? = null,
    @SerialName("bmi") val bmi: Double? = null,
    @SerialName("chest_circum") val chestCircum: Double? = null,
    @SerialName("waist_circum") val waistCircum: Double? = null,
    @SerialName("hip_circum") val hipCircum: Double? = null
)

@Serializable
data class FoodItem(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("unit_id") val unitId: Int? = null,
    @SerialName("calories_per_unit") val caloriesPerUnit: Double? = null,
    @SerialName("protein_per_unit") val proteinPerUnit: Double? = null,
    @SerialName("fat_per_unit") val fatPerUnit: Double? = null,
    @SerialName("carbs_per_unit") val carbsPerUnit: Double? = null
)

@Serializable
data class MealType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

@Serializable
data class Medication(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("unit_id") val unitId: Int? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("dosage") val dosage: String? = null,
    @SerialName("amount_medicine") val amountMedicine: Double? = null,
    @SerialName("acceptance_time") val acceptanceTime: String? = null,
    @SerialName("interval") val interval: String? = null
)

@Serializable
data class NutritionLog(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("food_id") val foodId: Int? = null,
    @SerialName("meal_type_id") val mealTypeId: Int? = null,
    @SerialName("quantity") val quantity: Double,
    @SerialName("date") val date: String
)

@Serializable
data class Steps(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("steps_count") val stepsCount: Int,
    @SerialName("date") val date: String
)

@Serializable
data class UserActivity(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("activity_type_id") val activityTypeId: Int? = null,
    @SerialName("duration_minutes") val durationMinutes: Int
)

@Serializable
data class WaterIntake(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String? = null,
    @SerialName("amount_ml") val amountMl: Double
)

// Motivation module
@Serializable
data class DailyPhoto(
    @SerialName("id") val id: Int,
    @SerialName("photo_url") val photoUrl: String,
    @SerialName("date") val date: String
)

@Serializable
data class DailyQuote(
    @SerialName("id") val id: Int,
    @SerialName("quote") val quote: String,
    @SerialName("date") val date: String,
    @SerialName("author_quote") val authorQuote: String = "Нет автора"
)

@Serializable
data class MoodMotivationalCard(
    @SerialName("id") val id: Int,
    @SerialName("card_id") val cardId: Int,
    @SerialName("mood_type_id") val moodTypeId: Int
)

@Serializable
data class MotivationalCard(
    @SerialName("id") val id: Int,
    @SerialName("quote_id") val quoteId: Int? = null,
    @SerialName("photo_id") val photoId: Int? = null
)

@Serializable
data class MotivationalUserCard(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("card_id") val cardId: Int,
    @SerialName("date") val date: String
)

// Public schema
@Serializable
data class DayOfTheWeek(
    @SerialName("id") val id: Int,
    @SerialName("day_weekly_name") val dayWeeklyName: String
)

@Serializable
data class DeletedRecord(
    @SerialName("id") val id: Int,
    @SerialName("original_id") val originalId: Int,
    @SerialName("table_name") val tableName: String,
    @SerialName("deleted_data") val deletedData: String,
    @SerialName("deleted_at") val deletedAt: String
)

@Serializable
data class Gender(
    @SerialName("id") val id: Int,
    @SerialName("gender_name") val genderName: String
)

@Serializable
data class Profile(
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("birth_date") val birthDate: String? = null,
    @SerialName("gender_id") val genderId: Int? = null,
    @SerialName("registration_date") val registrationDate: String,
    @SerialName("last_login") val lastLogin: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    @SerialName("friendship_code") val friendshipCode: String? = null
)

@Serializable
data class Unit(
    @SerialName("id") val id: Int,
    @SerialName("unit_name") val unitName: String
)

// Settings module
@Serializable
data class AppIcon(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

@Serializable
data class Theme(
    @SerialName("id") val id: Int,
    @SerialName("theme_name") val themeName: String,
    @SerialName("is_default") val isDefault: Boolean = false
)

@Serializable
data class UserSetting(
    @SerialName("user_id") val userId: String,
    @SerialName("id_theme") val idTheme: Int? = null,
    @SerialName("id_icon") val idIcon: Int? = null,
    @SerialName("created_at") val createdAt: String
)

// Social module
@Serializable
data class Friend(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("friend_id") val friendId: String,
    @SerialName("status_id") val statusId: Int? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class FriendshipStatus(
    @SerialName("id") val id: Int,
    @SerialName("status_name") val statusName: String
)

@Serializable
data class NotificationType(
    @SerialName("id") val id: Int,
    @SerialName("type_name") val typeName: String
)

@Serializable
data class Notification(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("initiator_id") val initiatorId: String? = null,
    @SerialName("type_id") val typeId: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("is_read") val isRead: Boolean = false,
    @SerialName("additional_data") val additionalData: String? = null
)

// Support module
@Serializable
data class SupportTicket(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: String? = null,
    @SerialName("email") val email: String,
    @SerialName("subject") val subject: String,
    @SerialName("description") val description: String? = null,
    @SerialName("id_status") val idStatus: Int? = null,
    @SerialName("id_priority") val idPriority: Int? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

@Serializable
data class TicketPriority(
    @SerialName("id") val id: Int,
    @SerialName("priority_name") val priorityName: String
)

@Serializable
data class TicketStatus(
    @SerialName("id") val id: Int,
    @SerialName("status_name") val statusName: String
)



