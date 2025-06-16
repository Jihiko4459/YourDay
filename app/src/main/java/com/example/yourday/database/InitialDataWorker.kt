package com.example.yourday.database

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InitialDataWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val KEY_DATA_LOADED = "is_initial_data_loaded"
    private val database = YourDayDatabase.getDatabase(context)

    override suspend fun doWork(): Result {
        return try {
            if (!prefs.getBoolean(KEY_DATA_LOADED, false)) {
                withContext(Dispatchers.IO) {
                    // Загрузка дней недели
                    database.loadInitialDays(database.dayOfTheWeekDao())

                    // Загрузка гендеров
                    database.insertAllReferenceGenders(database.genderDao())


                    // Загрузка типов активностей
                    database.addDefaultActivityTypes(database.activityTypeDao())


                    // Загрузка типов целей и привычек
                    database.insertAllReferenceGoalTypes(database.goalAndHabitTypeDao())

                    // Загрузка статусов целей
                    database.insertAllReferenceStatuses(database.goalStatusDao())

                    prefs.edit().putBoolean(KEY_DATA_LOADED, true).apply()
                }
            }
            else {
                Log.d("InitialDataWorker", "Data already loaded")
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("InitialDataWorker", "Error loading initial data", e)
            Result.retry()
        }
    }

    companion object {
        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<InitialDataWorker>()
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "initial_data_work",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}