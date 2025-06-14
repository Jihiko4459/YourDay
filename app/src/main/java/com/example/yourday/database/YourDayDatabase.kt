package com.example.yourday.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yourday.database.dao.DayOfTheWeekDao
import com.example.yourday.database.dao.DeletedRecordDao
import com.example.yourday.database.dao.GenderDao
import com.example.yourday.database.dao.SyncStatusDao
import com.example.yourday.database.dao.UnitDao
import com.example.yourday.database.dao.checklist.ChecklistCategoryDao
import com.example.yourday.database.dao.checklist.ChecklistItemDao
import com.example.yourday.database.dao.checklist.ItemMarkDao
import com.example.yourday.database.dao.content.ArticleCategoryDao
import com.example.yourday.database.dao.content.ArticleDao
import com.example.yourday.database.dao.content.ArticleInCategoryDao
import com.example.yourday.database.dao.content.ArticleStatusDao
import com.example.yourday.database.dao.content.UserArticleStatusDao
import com.example.yourday.database.dao.daily.DailyNoteDao
import com.example.yourday.database.dao.daily.GratitudeAndJoyJournalDao
import com.example.yourday.database.dao.daily.HobbyDao
import com.example.yourday.database.dao.daily.IdeaDao
import com.example.yourday.database.dao.finance.BalanceTypeDao
import com.example.yourday.database.dao.finance.TransactionDao
import com.example.yourday.database.dao.goals_and_habits.GoalAndHabitTypeDao
import com.example.yourday.database.dao.goals_and_habits.GoalDao
import com.example.yourday.database.dao.goals_and_habits.GoalStatusDao
import com.example.yourday.database.dao.goals_and_habits.HabitDao
import com.example.yourday.database.dao.health_and_fitness.ActivityTypeDao
import com.example.yourday.database.dao.health_and_fitness.BodyMeasurementDao
import com.example.yourday.database.dao.health_and_fitness.FoodItemDao
import com.example.yourday.database.dao.health_and_fitness.MealTypeDao
import com.example.yourday.database.dao.health_and_fitness.MedicationDao
import com.example.yourday.database.dao.health_and_fitness.NutritionLogDao
import com.example.yourday.database.dao.health_and_fitness.StepsDao
import com.example.yourday.database.dao.health_and_fitness.UserActivityDao
import com.example.yourday.database.dao.health_and_fitness.WaterIntakeDao
import com.example.yourday.database.dao.motivation.DailyPhotoDao
import com.example.yourday.database.dao.motivation.DailyQuoteDao
import com.example.yourday.database.dao.motivation.MotivationalCardDao
import com.example.yourday.database.dao.setting.AppIconDao
import com.example.yourday.database.dao.setting.ThemeDao
import com.example.yourday.database.dao.setting.UserSettingsDao
import com.example.yourday.database.dao.social.FriendDao
import com.example.yourday.database.dao.social.FriendshipStatusDao
import com.example.yourday.database.dao.social.NotificationDao
import com.example.yourday.database.dao.social.NotificationTypeDao
import com.example.yourday.database.dao.support.SupportTicketDao
import com.example.yourday.database.dao.support.TicketPriorityDao
import com.example.yourday.database.dao.support.TicketStatusDao
import com.example.yourday.database.dao.tasks.TaskDao
import com.example.yourday.database.dao.tasks.TaskDependencyDao
import com.example.yourday.database.dao.tasks.TaskPriorityTypeDao
import com.example.yourday.database.dao.tasks.TaskTypeDao
import com.example.yourday.model.LocalActivityType
import com.example.yourday.model.LocalAppIcon
import com.example.yourday.model.LocalArticle
import com.example.yourday.model.LocalArticleCategory
import com.example.yourday.model.LocalArticleInCategory
import com.example.yourday.model.LocalArticleStatus
import com.example.yourday.model.LocalBalanceType
import com.example.yourday.model.LocalBodyMeasurement
import com.example.yourday.model.LocalChecklistCategory
import com.example.yourday.model.LocalChecklistItem
import com.example.yourday.model.LocalDailyNote
import com.example.yourday.model.LocalDailyPhoto
import com.example.yourday.model.LocalDailyQuote
import com.example.yourday.model.LocalDayOfTheWeek
import com.example.yourday.model.LocalDeletedRecord
import com.example.yourday.model.LocalFoodItem
import com.example.yourday.model.LocalFriend
import com.example.yourday.model.LocalFriendshipStatus
import com.example.yourday.model.LocalGender
import com.example.yourday.model.LocalGoal
import com.example.yourday.model.LocalGoalAndHabitType
import com.example.yourday.model.LocalGoalStatus
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import com.example.yourday.model.LocalHabit
import com.example.yourday.model.LocalHobby
import com.example.yourday.model.LocalIdea
import com.example.yourday.model.LocalItemMark
import com.example.yourday.model.LocalMealType
import com.example.yourday.model.LocalMedication
import com.example.yourday.model.LocalMotivationalCard
import com.example.yourday.model.LocalNotification
import com.example.yourday.model.LocalNotificationType
import com.example.yourday.model.LocalNutritionLog
import com.example.yourday.model.LocalSteps
import com.example.yourday.model.LocalSupportTicket
import com.example.yourday.model.LocalTask
import com.example.yourday.model.LocalTaskDependency
import com.example.yourday.model.LocalTaskPriorityType
import com.example.yourday.model.LocalTaskType
import com.example.yourday.model.LocalTheme
import com.example.yourday.model.LocalTicketPriority
import com.example.yourday.model.LocalTicketStatus
import com.example.yourday.model.LocalTransaction
import com.example.yourday.model.LocalUnit
import com.example.yourday.model.LocalUserActivity
import com.example.yourday.model.LocalUserArticleStatus
import com.example.yourday.model.LocalUserSettings
import com.example.yourday.model.LocalWaterIntake
import com.example.yourday.model.SyncStatus


@Database(
    entities = [
        LocalUserSettings::class,
        LocalActivityType::class,
        LocalAppIcon::class,
        LocalArticleCategory::class,
        LocalArticleStatus::class,
        LocalArticle::class,
        LocalArticleInCategory::class,
        LocalBalanceType::class,
        LocalBodyMeasurement::class,
        LocalChecklistCategory::class,
        LocalChecklistItem::class,
        LocalDailyNote::class,
        LocalDailyPhoto::class,
        LocalDailyQuote::class,
        LocalDayOfTheWeek::class,
        LocalDeletedRecord::class,
        LocalFoodItem::class,
        LocalFriend::class,
        LocalFriendshipStatus::class,
        LocalGender::class,
        LocalGoalAndHabitType::class,
        LocalGoalStatus::class,
        LocalGoal::class,
        LocalGratitudeAndJoyJournal::class,
        LocalHabit::class,
        LocalHobby::class,
        LocalIdea::class,
        LocalItemMark::class,
        LocalMealType::class,
        LocalMedication::class,
        LocalMotivationalCard::class,
        LocalNotificationType::class,
        LocalNotification::class,
        LocalNutritionLog::class,
        LocalSteps::class,
        LocalSupportTicket::class,
        LocalTaskDependency::class,
        LocalTaskPriorityType::class,
        LocalTaskType::class,
        LocalTask::class,
        LocalTheme::class,
        LocalTicketPriority::class,
        LocalTicketStatus::class,
        LocalTransaction::class,
        LocalUnit::class,
        LocalUserActivity::class,
        LocalUserArticleStatus::class,
        LocalWaterIntake::class,
        SyncStatus::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class YourDayDatabase : RoomDatabase() {
    // User Settings
    abstract fun userSettingsDao(): UserSettingsDao

    // Activity
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun userActivityDao(): UserActivityDao

    // App
    abstract fun appIconDao(): AppIconDao
    abstract fun themeDao(): ThemeDao

    // Articles
    abstract fun articleDao(): ArticleDao
    abstract fun articleCategoryDao(): ArticleCategoryDao
    abstract fun articleStatusDao(): ArticleStatusDao
    abstract fun articleInCategoryDao(): ArticleInCategoryDao
    abstract fun userArticleStatusDao(): UserArticleStatusDao

    // Body
    abstract fun bodyMeasurementDao(): BodyMeasurementDao

    // Checklist
    abstract fun checklistCategoryDao(): ChecklistCategoryDao
    abstract fun checklistItemDao(): ChecklistItemDao

    // Daily
    abstract fun dailyNoteDao(): DailyNoteDao


    // Days
    abstract fun dayOfTheWeekDao(): DayOfTheWeekDao

    // Deleted Records
    abstract fun deletedRecordDao(): DeletedRecordDao

    // Food
    abstract fun foodItemDao(): FoodItemDao
    abstract fun mealTypeDao(): MealTypeDao
    abstract fun nutritionLogDao(): NutritionLogDao
    abstract fun unitDao(): UnitDao

    // Friends
    abstract fun friendDao(): FriendDao
    abstract fun friendshipStatusDao(): FriendshipStatusDao

    // Gender
    abstract fun genderDao(): GenderDao

    // Goals & Habits
    abstract fun goalAndHabitTypeDao(): GoalAndHabitTypeDao
    abstract fun goalStatusDao(): GoalStatusDao
    abstract fun goalDao(): GoalDao
    abstract fun habitDao(): HabitDao

    // Gratitude & Joy
    abstract fun gratitudeAndJoyJournalDao(): GratitudeAndJoyJournalDao

    // Hobbies
    abstract fun hobbyDao(): HobbyDao

    // Ideas
    abstract fun ideaDao(): IdeaDao

    // Item Marks
    abstract fun itemMarkDao(): ItemMarkDao

    // Medications
    abstract fun medicationDao(): MedicationDao



    // Motivational
    abstract fun dailyPhotoDao(): DailyPhotoDao
    abstract fun dailyQuoteDao(): DailyQuoteDao
    abstract fun motivationalCardDao(): MotivationalCardDao

    // Notifications
    abstract fun notificationTypeDao(): NotificationTypeDao
    abstract fun notificationDao(): NotificationDao

    // Steps
    abstract fun stepsDao(): StepsDao

    // Support
    abstract fun supportTicketDao(): SupportTicketDao
    abstract fun ticketPriorityDao(): TicketPriorityDao
    abstract fun ticketStatusDao(): TicketStatusDao

    // Tasks
    abstract fun taskDao(): TaskDao
    abstract fun taskTypeDao(): TaskTypeDao
    abstract fun taskPriorityTypeDao(): TaskPriorityTypeDao
    abstract fun taskDependencyDao(): TaskDependencyDao

    // Transactions
    abstract fun transactionDao(): TransactionDao
    abstract fun balanceTypeDao(): BalanceTypeDao

    // Water
    abstract fun waterIntakeDao(): WaterIntakeDao



    // Sync
    abstract fun syncStatusDao(): SyncStatusDao

    companion object {
        @Volatile
        private var INSTANCE: YourDayDatabase? = null

        fun getDatabase(context: Context): YourDayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YourDayDatabase::class.java,
                    "yourday_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}