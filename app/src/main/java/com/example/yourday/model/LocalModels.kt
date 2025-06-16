// LocalModels.kt - Models for Room database
package com.example.yourday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import java.util.Date


// Activity Types
@Entity(tableName = "activity_types")
data class LocalActivityType(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val caloriesPerMin: Double? = null,
    val iconType: String? = null
)


// Article Categories
@Entity(tableName = "article_categories")
data class LocalArticleCategory(
    @PrimaryKey val id: Int = 0,
    val categoryName: String
)

// Article Statuses
@Entity(tableName = "article_statuses")
data class LocalArticleStatus(
    @PrimaryKey val id: Int = 0,
    val statusName: String
)

// Articles
@Entity(tableName = "articles")
data class LocalArticle(
    @PrimaryKey val id: Int = 0,
    val title: String,
    val content: String? = null,
    val articleImage: String? = null,
    val createdAt: String
)

// Articles in Categories
@Entity(tableName = "articles_in_categories")
data class LocalArticleInCategory(
    @PrimaryKey val id: Int = 0,
    val articleId: Int? = null,
    val categoryId: Int? = null
)

// Balance Types
@Entity(tableName = "balance_types")
data class LocalBalanceType(
    @PrimaryKey val id: Int = 0,
    val typeName: String
)

// Body Measurements
@Entity(tableName = "body_measurements")
data class LocalBodyMeasurement(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val weight: Double? = null,
    val height: Double? = null,
    val bmi: Double? = null,
    val chestCircum: Double? = null,
    val waistCircum: Double? = null,
    val hipCircum: Double? = null
)

// Checklist Categories
@Entity(tableName = "checklist_categories")
data class LocalChecklistCategory(
    @PrimaryKey val id: Int = 0,
    val categoryName: String,
    val icon: String? = null
)

// Checklist Items
@Entity(tableName = "checklist_items")
data class LocalChecklistItem(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val categoryId: Int? = null,
    val itemName: String
)

// Daily Notes
@Entity(tableName = "daily_notes")
data class LocalDailyNote(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val note: String? = null,
    val lastModified: Long = System.currentTimeMillis()
)

// Daily Photos
@Entity(tableName = "daily_photos")
data class LocalDailyPhoto(
    @PrimaryKey val id: Int = 0,
    val photoUrl: String,
    val date: String
)

// Daily Quotes
@Entity(tableName = "daily_quotes")
data class LocalDailyQuote(
    @PrimaryKey val id: Int = 0,
    val quote: String,
    val date: String,
    val authorQuote: String = "Нет автора"
)

// Days of the Week
@Entity(tableName = "days_of_the_week")
data class LocalDayOfTheWeek(
    @PrimaryKey val id: Int = 0,
    val dayWeeklyName: String
)

// Food Items
@Entity(tableName = "food_items")
data class LocalFoodItem(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val unitId: Int? = null,
    val caloriesPerUnit: Double? = null,
    val proteinPerUnit: Double? = null,
    val fatPerUnit: Double? = null,
    val carbsPerUnit: Double? = null
)

// Genders
@Entity(tableName = "genders")
data class LocalGender(
    @PrimaryKey val id: Int = 0,
    val genderName: String
)

// Goal and Habit Types
@Entity(tableName = "goal_and_habit_types")
data class LocalGoalAndHabitType(
    @PrimaryKey val id: Int = 0,
    val typeName: String,
    val iconType: String? = null
)

// Goal Statuses
@Entity(tableName = "goal_statuses")
data class LocalGoalStatus(
    @PrimaryKey val id: Int = 0,
    val statusName: String
)

// Goals
@Entity(tableName = "goals")
data class LocalGoal(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val title: String,
    val description: String? = null,
    val goalTypeId: Int? = null,
    val statusId: Int? = null,
    val progressGoal: Double = 0.0,
    val createdAt: String,
    val achievedAt: String? = null,
    val lastUpdated: String,
    val lastModified: Long = System.currentTimeMillis()
)

// Gratitude and Joy Journals
@Entity(tableName = "gratitude_and_joy_journals")
data class LocalGratitudeAndJoyJournal(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val gratitude: String? = null,
    val joy: String? = null,
    val lastModified: Long = System.currentTimeMillis()
)

// Habits
@Entity(tableName = "habits")
data class LocalHabit(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val title: String,
    val habitTypeId: Int? = null,
    val createdAt: String
)

// Hobbies
@Entity(tableName = "hobbies")
data class LocalHobby(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val name: String,
    val description: String? = null,
    val day1Id: Int? = null,
    val day2Id: Int? = null,
    val day3Id: Int? = null
)

// Ideas
@Entity(tableName = "ideas")
data class LocalIdea(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val title: String,
    val description: String? = null
)

// Item Marks
@Entity(tableName = "item_marks")
data class LocalItemMark(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val itemId: Int,
    val date: String,
    val time: String? = null,
    val isTaken: Boolean = false
)

// Meal Types
@Entity(tableName = "meal_types")
data class LocalMealType(
    @PrimaryKey val id: Int = 0,
    val typeName: String
)

// Medications
@Entity(tableName = "medications")
data class LocalMedication(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val name: String,
    val unitId: Int? = null,
    val description: String? = null,
    val dosage: String? = null,
    val amountMedicine: Double? = null,
    val acceptanceTime: String? = null,
    val interval: String? = null
)

// Motivational Cards
@Entity(tableName = "motivational_cards")
data class LocalMotivationalCard(
    @PrimaryKey val id: Int = 0,
    val quoteId: Int? = null,
    val photoId: Int? = null
)


// Nutrition Log
@Entity(tableName = "nutrition_logs")
data class LocalNutritionLog(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val foodId: Int? = null,
    val mealTypeId: Int? = null,
    val quantity: Double,
    val date: String
)

// Steps
@Entity(tableName = "steps")
data class LocalSteps(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val stepsCount: Int,
    val date: String
)

// Task Dependencies
@Entity(tableName = "task_dependencies")
data class LocalTaskDependency(
    @PrimaryKey val id: Int = 0,
    val taskId: Int? = null,
    val dependsOn: Int? = null,
    val orderBy: Int? = null
)

// Task Priority Types
@Entity(tableName = "task_priority_types")
data class LocalTaskPriorityType(
    @PrimaryKey val id: Int = 0,
    val typeName: String
)

// Task Types
@Entity(tableName = "task_types")
data class LocalTaskType(
    @PrimaryKey val id: Int = 0,
    val typeName: String,
    val typeIcon: String? = null
)

// Tasks
@Entity(tableName = "tasks")
data class LocalTask(
    @PrimaryKey val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean,
    val userId: String,
    val createdAt: Long
)

// Transactions
@Entity(tableName = "transactions")
data class LocalTransaction(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val title: String,
    val description: String? = null,
    val balanceTypeId: Int? = null,
    val amount: Double,
    val date: String
)

// Units
@Entity(tableName = "units")
data class LocalUnit(
    @PrimaryKey val id: Int = 0,
    val unitName: String
)

// User Activity
@Entity(tableName = "user_activities")
data class LocalUserActivity(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val activityTypeId: Int? = null,
    val durationMinutes: Int
)

// User Article Statuses
@Entity(tableName = "user_article_statuses")
data class LocalUserArticleStatus(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val articleId: Int? = null,
    val statusId: Int? = null,
    val statusChangedAt: String
)

// Water Intake
@Entity(tableName = "water_intake")
data class LocalWaterIntake(
    @PrimaryKey val id: Int = 0,
    val userId: String,
    val date: String,
    val time: String? = null,
    val amountMl: Double
)