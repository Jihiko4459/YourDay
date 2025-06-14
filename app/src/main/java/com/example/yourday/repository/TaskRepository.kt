//package com.example.yourday.repository
//
//import co.touchlab.kermit.Logger
//import com.example.yourday.api.SupabaseHelper
//import com.example.yourday.database.YourDayDatabase
//import com.example.yourday.database.dao.SyncStatusDao
//import com.example.yourday.database.dao.tasks.TaskDao
//import com.example.yourday.model.LocalTask
//import com.example.yourday.model.Task
//import io.github.jan.supabase.postgrest.from
//import kotlinx.coroutines.flow.Flow
//import java.text.SimpleDateFormat
//import java.util.Date
//
//
//class TaskRepository(
//    private val db: YourDayDatabase,
//    private val supabaseHelper: SupabaseHelper
//) {
//    fun getByUser(userId: String): Flow<List<LocalTask>> {
//        return db.taskDao().getByUser(userId)
//    }
//
//    suspend fun addTask(task: LocalTask) {
//        db.taskDao().insert(task)
//        syncTasks()
//    }
//
//    private suspend fun syncTasks() {
//        val unsynced = db.taskDao().getUnsynced()
//        unsynced.forEach { task ->
//            try {
//                val result = supabaseHelper.withAuth {
//                    supabaseHelper.client.from("tasks").upsert(
//                        Task(
//                            id = task.serverId ?: 0,
//                            userId = task.userId,
//                            title = task.title,
//                            description = task.description,
//                            taskTypeId = task.taskTypeId,
//                            priorityId = task.priorityId,
//                            createdAt = task.createdAt,
//                            dueDate = task.dueDate,
//                            isDependent = task.isDependent,
//                            isCompleted = task.isCompleted,
//                            completedAt = task.completedAt
//                        )
//                    )
//                }
//
//                if (result.isSuccess) {
//                    db.taskDao().update(task.copy(isSynced = true))
//                }
//            } catch (e: Exception) {
//                // Error handling
//            }
//        }
//    }
//}