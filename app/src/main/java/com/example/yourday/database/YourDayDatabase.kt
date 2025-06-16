package com.example.yourday.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yourday.database.dao.DayOfTheWeekDao
import com.example.yourday.database.dao.GenderDao
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
import com.example.yourday.database.dao.tasks.TaskDao
import com.example.yourday.database.dao.tasks.TaskDependencyDao
import com.example.yourday.database.dao.tasks.TaskPriorityTypeDao
import com.example.yourday.database.dao.tasks.TaskTypeDao
import com.example.yourday.model.LocalActivityType
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
import com.example.yourday.model.LocalFoodItem
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
import com.example.yourday.model.LocalNutritionLog
import com.example.yourday.model.LocalSteps
import com.example.yourday.model.LocalTask
import com.example.yourday.model.LocalTaskDependency
import com.example.yourday.model.LocalTaskPriorityType
import com.example.yourday.model.LocalTaskType
import com.example.yourday.model.LocalTransaction
import com.example.yourday.model.LocalUnit
import com.example.yourday.model.LocalUserActivity
import com.example.yourday.model.LocalUserArticleStatus
import com.example.yourday.model.LocalWaterIntake


@Database(
    entities = [
        LocalActivityType::class,
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
        LocalFoodItem::class,
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
        LocalNutritionLog::class,
        LocalSteps::class,
        LocalTaskDependency::class,
        LocalTaskPriorityType::class,
        LocalTaskType::class,
        LocalTask::class,
        LocalTransaction::class,
        LocalUnit::class,
        LocalUserActivity::class,
        LocalUserArticleStatus::class,
        LocalWaterIntake::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class YourDayDatabase : RoomDatabase() {
    // Activity
    abstract fun activityTypeDao(): ActivityTypeDao
    abstract fun userActivityDao(): UserActivityDao


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

    // Food
    abstract fun foodItemDao(): FoodItemDao
    abstract fun mealTypeDao(): MealTypeDao
    abstract fun nutritionLogDao(): NutritionLogDao
    abstract fun unitDao(): UnitDao

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

    // Steps
    abstract fun stepsDao(): StepsDao



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
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("YourDayDatabase", "Database created")
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

    // Helper functions for initial data loading
    suspend fun loadInitialDays(dao: DayOfTheWeekDao) {
        val initialDays = listOf(
            LocalDayOfTheWeek(id = 1, dayWeeklyName = "Понедельник"),
            LocalDayOfTheWeek(id = 2, dayWeeklyName = "Вторник"),
            LocalDayOfTheWeek(id = 3, dayWeeklyName = "Среда"),
            LocalDayOfTheWeek(id = 4, dayWeeklyName = "Четверг"),
            LocalDayOfTheWeek(id = 5, dayWeeklyName = "Пятница"),
            LocalDayOfTheWeek(id = 6, dayWeeklyName = "Суббота"),
            LocalDayOfTheWeek(id = 7, dayWeeklyName = "Воскресенье")
        )
        dao.deleteAll()
        initialDays.forEach { day -> dao.upsert(day) }
    }

    suspend fun insertAllReferenceGenders(dao: GenderDao) {
        val referenceGenders = listOf(
            LocalGender(1, "Женский"),
            LocalGender(2, "Мужской")
        )
        dao.insertAll(referenceGenders)
    }

    suspend fun addDefaultActivityTypes(dao: ActivityTypeDao) {
        val defaultTypes = listOf(
            LocalActivityType(
                id = 1,
                name = "Бег",
                caloriesPerMin = 15.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//running_vgwo4ekd04xl%201.svg"
            ),
            LocalActivityType(
                id = 2,
                name = "Ходьба",
                caloriesPerMin = 6.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//walk_h6da4yqbend6%201.svg"
            ),
            LocalActivityType(
                id = 3,
                name = "Велоспорт",
                caloriesPerMin = 10.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//cyclist_22i4htgezad9%201.svg"
            ),
            LocalActivityType(
                id = 4,
                name = "Плавание",
                caloriesPerMin = 9.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//swimming_dsp856fod376%201.svg"
            ),
            LocalActivityType(
                id = 5,
                name = "Йога",
                caloriesPerMin = 5.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//Group.svg"
            ),
            LocalActivityType(
                id = 6,
                name = "Тренировка",
                caloriesPerMin = 10.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dumbbells_0rljf1lyt3gw%201.svg"
            ),
            LocalActivityType(
                id = 7,
                name = "Поход в магазин",
                caloriesPerMin = 4.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//shopping_cart_kn65d3gmkbvj%201.svg"
            ),
            LocalActivityType(
                id = 8,
                name = "Выгул собаки",
                caloriesPerMin = 5.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dog_fals6qp13l3h%201.svg"
            ),
            LocalActivityType(
                id = 9,
                name = "Уборка дома",
                caloriesPerMin = 5.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//broom_2ebf73plnojg%201.svg"
            ),
            LocalActivityType(
                id = 10,
                name = "Поход",
                caloriesPerMin = 7.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//camping_09eo57zvdr80%201.svg"
            ),
            LocalActivityType(
                id = 11,
                name = "Рыбалка",
                caloriesPerMin = 4.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//fishing_ohanh408p7ac%201.svg"
            ),
            LocalActivityType(
                id = 12,
                name = "Лыжи",
                caloriesPerMin = 12.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//skier_j1pwxf2vhlsa%201.svg"
            ),
            LocalActivityType(
                id = 13,
                name = "Танцы",
                caloriesPerMin = 8.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//dancing_bwj9463nfjr9%201.svg"
            ),
            LocalActivityType(
                id = 14,
                name = "Актерское мастерство",
                caloriesPerMin = 4.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//theatre_xhpbtryeeih0%201.svg"
            ),
            LocalActivityType(
                id = 15,
                name = "Игра на инструменте",
                caloriesPerMin = 3.0,
                iconType = "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/activity-type-icons//guitar_1n96kd6ki4lg%201.svg"
            )
        )
        dao.deleteAll()
        defaultTypes.forEach { type -> dao.upsert(type) }
    }


    suspend fun insertAllReferenceGoalTypes(dao: GoalAndHabitTypeDao) {
        val referenceTypes = listOf(
            LocalGoalAndHabitType(1, "Книги", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS5zdmciLCJpYXQiOjE3NDkyNzY0NzYsImV4cCI6MTc4MDgxMjQ3Nn0.s4qKaDsxr7Zhb2Ep6bp15Nqfr5-ubXIjIoB8c4dzTPg"),
            LocalGoalAndHabitType(2, "Здоровье", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/healthy-recognition.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9oZWFsdGh5LXJlY29nbml0aW9uLnN2ZyIsImlhdCI6MTc0OTI3NjQ5MiwiZXhwIjoxNzgwODEyNDkyfQ.yiMmzxIG5sIUSlvNLcTIWETWx9welQKTQliaPKeMEps"),
            LocalGoalAndHabitType(3, "Обучение", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Education.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9FZHVjYXRpb24uc3ZnIiwiaWF0IjoxNzQ5Mjc2NDk5LCJleHAiOjE3ODA4MTI0OTl9.YkSQp2Ns7qP1IoadtytUP7uLHMcaXVLrgnWXsLMNA-M"),
            LocalGoalAndHabitType(4, "Творчество", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/masks-theater.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9tYXNrcy10aGVhdGVyLnN2ZyIsImlhdCI6MTc0OTI3NjUxMSwiZXhwIjoxNzgwODEyNTExfQ.2cQzTRh84_0xM8b10ZEYG7nAC89nRDYMjJDdbVvXjAc"),
            LocalGoalAndHabitType(5, "Деньги", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-1.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS0xLnN2ZyIsImlhdCI6MTc0OTI3NjUyMCwiZXhwIjoxNzgwODEyNTIwfQ.1EGS1N_en2U99wtCgAuRfz7gHUfhq8ijQOF1hxKxgAo"),
            LocalGoalAndHabitType(6, "Работа", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/briefcase.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9icmllZmNhc2Uuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTM0LCJleHAiOjE3ODA4MTI1MzR9.flxtO-uGOO0nml32ee4z7CLJzc2TewkUWeEuuf-pEBc"),
            LocalGoalAndHabitType(7, "Семья", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/family.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9mYW1pbHkuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQxLCJleHAiOjE3ODA4MTI1NDF9.xi09ZYTFsOSkgx7ZpXNGzLjjbeQDKsNK3nFXENWAdC8"),
            LocalGoalAndHabitType(8, "Общение", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/chatbubble-ellipses-outline.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9jaGF0YnViYmxlLWVsbGlwc2VzLW91dGxpbmUuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQ5LCJleHAiOjE3ODA4MTI1NDl9.IawzsQY9jpSUQdHg6HL1f0USPkTEV5YgonJeMux6TYs"),
            LocalGoalAndHabitType(9, "Личные вызовы", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-2.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS0yLnN2ZyIsImlhdCI6MTc0OTI3NjU1NCwiZXhwIjoxNzgwODEyNTU0fQ.1A3wWlbqU3N-lT1OHjeRu-vemXOUX3AjOWyXrKf-zwE")
        )
        dao.insertAll(referenceTypes)
    }

    suspend fun insertAllReferenceStatuses(dao: GoalStatusDao) {
        val referenceStatuses = listOf(
            LocalGoalStatus(1, "Не начата"),
            LocalGoalStatus(2, "В процессе"),
            LocalGoalStatus(3, "Завершена")
        )
        dao.insertAll(referenceStatuses)
    }


    suspend fun insertAllReferenceUnits(dao: UnitDao) {
        val referenceUnits = listOf(
            LocalUnit(1, "таб."),
            LocalUnit(2, "пак."),
            LocalUnit(3, "раз"),
            LocalUnit(4, "укол"),
            LocalUnit(5, "кап."),
            LocalUnit(6, "г"),
            LocalUnit(7, "мл"),
            LocalUnit(8, "ч"),
            LocalUnit(9, "мин"),
            LocalUnit(10, "км"),
            LocalUnit(11, "шаг"),
            LocalUnit(12, "кг"),
            LocalUnit(13, "см")
        )
        referenceUnits.forEach { unit -> dao.upsert(unit) }
    }
}