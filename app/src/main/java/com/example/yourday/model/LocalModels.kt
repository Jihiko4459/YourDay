// LocalModels.kt - Models for Room database
package com.example.yourday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Base interface for all local entities
interface LocalEntity {
    val localId: Int
    val serverId: Int?
    val isSynced: Boolean
}

// User settings
@Entity(tableName = "user_settings")
data class LocalUserSettings(
    @PrimaryKey val userId: String,
    val themeId: Int? = null,
    val appIconId: Int? = null,
    val createdAt: String,
    val isSynced: Boolean?=null

)

// Activity Types
@Entity(tableName = "activity_types")
data class LocalActivityType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val name: String,
    val caloriesPerMin: Double? = null,
    val iconType: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// App Icons
@Entity(tableName = "app_icons")
data class LocalAppIcon(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val name: String,
    val url: String? = null,
    val isActive: Boolean = true,
    override val isSynced: Boolean = false
) : LocalEntity

// Article Categories
@Entity(tableName = "article_categories")
data class LocalArticleCategory(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val categoryName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Article Statuses
@Entity(tableName = "article_statuses")
data class LocalArticleStatus(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val statusName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Articles
@Entity(tableName = "articles")
data class LocalArticle(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val title: String,
    val content: String? = null,
    val articleImage: String? = null,
    val createdAt: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Articles in Categories
@Entity(tableName = "articles_in_categories")
data class LocalArticleInCategory(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val articleId: Int? = null,
    val categoryId: Int? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Balance Types
@Entity(tableName = "balance_types")
data class LocalBalanceType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Body Measurements
@Entity(tableName = "body_measurements")
data class LocalBodyMeasurement(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val weight: Double? = null,
    val height: Double? = null,
    val bmi: Double? = null,
    val chestCircum: Double? = null,
    val waistCircum: Double? = null,
    val hipCircum: Double? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Checklist Categories
@Entity(tableName = "checklist_categories")
data class LocalChecklistCategory(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val categoryName: String,
    val icon: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Checklist Items
@Entity(tableName = "checklist_items")
data class LocalChecklistItem(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val categoryId: Int? = null,
    val itemName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Daily Notes
@Entity(tableName = "daily_notes")
data class LocalDailyNote(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val note: String? = null,
    override val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) : LocalEntity

// Daily Photos
@Entity(tableName = "daily_photos")
data class LocalDailyPhoto(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val photoUrl: String,
    val date: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Daily Quotes
@Entity(tableName = "daily_quotes")
data class LocalDailyQuote(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val quote: String,
    val date: String,
    val authorQuote: String = "Нет автора",
    override val isSynced: Boolean = false
) : LocalEntity

// Days of the Week
@Entity(tableName = "days_of_the_week")
data class LocalDayOfTheWeek(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val dayWeeklyName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Deleted Records
@Entity(tableName = "deleted_records")
data class LocalDeletedRecord(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val originalId: Int,
    val tableName: String,
    val deletedData: String? = null,
    val deletedAt: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Food Items
@Entity(tableName = "food_items")
data class LocalFoodItem(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val name: String,
    val unitId: Int? = null,
    val caloriesPerUnit: Double? = null,
    val proteinPerUnit: Double? = null,
    val fatPerUnit: Double? = null,
    val carbsPerUnit: Double? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Friends
@Entity(tableName = "friends")
data class LocalFriend(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val friendId: String,
    val statusId: Int? = null,
    val createdAt: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Friendship Statuses
@Entity(tableName = "friendship_statuses")
data class LocalFriendshipStatus(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val statusName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Genders
@Entity(tableName = "genders")
data class LocalGender(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val genderName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Goal and Habit Types
@Entity(tableName = "goal_and_habit_types")
data class LocalGoalAndHabitType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    val iconType: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Goal Statuses
@Entity(tableName = "goal_statuses")
data class LocalGoalStatus(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val statusName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Goals
@Entity(tableName = "goals")
data class LocalGoal(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val title: String,
    val description: String? = null,
    val goalTypeId: Int? = null,
    val statusId: Int? = null,
    val progressGoal: Double = 0.0,
    val createdAt: String,
    val achievedAt: String? = null,
    val lastUpdated: String,
    override val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) : LocalEntity

// Gratitude and Joy Journals
@Entity(tableName = "gratitude_and_joy_journals")
data class LocalGratitudeAndJoyJournal(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val gratitude: String? = null,
    val joy: String? = null,
    override val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) : LocalEntity

// Habits
@Entity(tableName = "habits")
data class LocalHabit(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val title: String,
    val habitTypeId: Int? = null,
    val createdAt: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Hobbies
@Entity(tableName = "hobbies")
data class LocalHobby(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val name: String,
    val description: String? = null,
    val day1Id: Int? = null,
    val day2Id: Int? = null,
    val day3Id: Int? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Ideas
@Entity(tableName = "ideas")
data class LocalIdea(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val title: String,
    val description: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Item Marks
@Entity(tableName = "item_marks")
data class LocalItemMark(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val itemId: Int,
    val date: String,
    val time: String? = null,
    val isTaken: Boolean = false,
    override val isSynced: Boolean = false
) : LocalEntity

// Meal Types
@Entity(tableName = "meal_types")
data class LocalMealType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Medications
@Entity(tableName = "medications")
data class LocalMedication(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val name: String,
    val unitId: Int? = null,
    val description: String? = null,
    val dosage: String? = null,
    val amountMedicine: Double? = null,
    val acceptanceTime: String? = null,
    val interval: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity




// Motivational Cards
@Entity(tableName = "motivational_cards")
data class LocalMotivationalCard(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val quoteId: Int? = null,
    val photoId: Int? = null,
    override val isSynced: Boolean = false
) : LocalEntity



// Notification Types
@Entity(tableName = "notification_types")
data class LocalNotificationType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Notifications
@Entity(tableName = "notifications")
data class LocalNotification(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val initiatorId: String? = null,
    val typeId: Int? = null,
    val message: String? = null,
    val createdAt: String,
    val isRead: Boolean = false,
    val additionalData: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Nutrition Log
@Entity(tableName = "nutrition_logs")
data class LocalNutritionLog(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val foodId: Int? = null,
    val mealTypeId: Int? = null,
    val quantity: Double,
    val date: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Steps
@Entity(tableName = "steps")
data class LocalSteps(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val stepsCount: Int,
    val date: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Support Tickets
@Entity(tableName = "support_tickets")
data class LocalSupportTicket(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String? = null,
    val email: String,
    val subject: String,
    val description: String? = null,
    val idStatus: Int? = null,
    val idPriority: Int? = null,
    val createdAt: String,
    val updatedAt: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Task Dependencies
@Entity(tableName = "task_dependencies")
data class LocalTaskDependency(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val taskId: Int? = null,
    val dependsOn: Int? = null,
    val orderBy: Int? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Task Priority Types
@Entity(tableName = "task_priority_types")
data class LocalTaskPriorityType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Task Types
@Entity(tableName = "task_types")
data class LocalTaskType(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val typeName: String,
    val typeIcon: String? = null,
    override val isSynced: Boolean = false
) : LocalEntity

// Tasks
@Entity(tableName = "tasks")
data class LocalTask(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val title: String,
    val description: String? = null,
    val taskTypeId: Int? = null,
    val priorityId: Int? = null,
    val createdAt: String,
    val dueDate: String? = null,
    val isDependent: Boolean = false,
    val isCompleted: Boolean = false,
    val completedAt: String? = null,
    override val isSynced: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) : LocalEntity

// Themes
@Entity(tableName = "themes")
data class LocalTheme(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val themeName: String,
    val isDefault: Boolean = false,
    override val isSynced: Boolean = false
) : LocalEntity

// Ticket Priorities
@Entity(tableName = "ticket_priorities")
data class LocalTicketPriority(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val priorityName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Ticket Statuses
@Entity(tableName = "ticket_statuses")
data class LocalTicketStatus(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val statusName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Transactions
@Entity(tableName = "transactions")
data class LocalTransaction(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val title: String,
    val description: String? = null,
    val balanceTypeId: Int? = null,
    val amount: Double,
    val date: String,
    override val isSynced: Boolean = false
) : LocalEntity

// Units
@Entity(tableName = "units")
data class LocalUnit(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val unitName: String,
    override val isSynced: Boolean = false
) : LocalEntity

// User Activity
@Entity(tableName = "user_activities")
data class LocalUserActivity(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val activityTypeId: Int? = null,
    val durationMinutes: Int,
    override val isSynced: Boolean = false
) : LocalEntity

// User Article Statuses
@Entity(tableName = "user_article_statuses")
data class LocalUserArticleStatus(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val articleId: Int? = null,
    val statusId: Int? = null,
    val statusChangedAt: String,
    override val isSynced: Boolean = false
) : LocalEntity






// Water Intake
@Entity(tableName = "water_intake")
data class LocalWaterIntake(
    @PrimaryKey override val localId: Int = 0,
    override val serverId: Int? = null,
    val userId: String,
    val date: String,
    val time: String? = null,
    val amountMl: Double,
    override val isSynced: Boolean = false
) : LocalEntity


// Sync status tracking
@Entity(tableName = "sync_status")
data class SyncStatus(
    @PrimaryKey val tableName: String,
    val lastSyncTime: Long,
    val pendingChanges: Boolean
)