//import android.content.Context
//import com.example.yourday.api.SupabaseHelper
//import com.example.yourday.database.YourDauDatabase
//import com.example.yourday.model.SyncStatus
//
//class SyncManager(
//    private val db: YourDauDatabase,
//    private val supabaseHelper: SupabaseHelper,
//    private val context: Context
//) {
//    suspend fun syncAllData(userId: String) {
//        if (!supabaseHelper.ensureAuthenticated()) return
//
//        // Сначала отправляем локальные изменения на сервер
//        pushLocalChanges()
//
//        // Затем загружаем свежие данные с сервера
//        pullServerChanges(userId)
//    }
//
//    private suspend fun pushLocalChanges() {
//        // Синхронизируем все сущности
//        syncNotes()
//        syncTasks()
//        syncGoals()
//        // ... другие сущности
//    }
//
//    private suspend fun pullServerChanges(userId: String) {
//        val lastSyncTime = db.syncStatusDao().getLastSyncTime("all") ?: 0
//
//        // Загружаем свежие данные с сервера
//        val serverNotes = supabaseHelper.getDailyNotesSince(lastSyncTime, userId)
//        val serverTasks = supabaseHelper.getTasksSince(lastSyncTime, userId)
//        // ... другие сущности
//
//        // Сохраняем в локальную базу
//        db.dailyNoteDao().insertAll(serverNotes.map { it.toLocal() })
//        db.taskDao().insertAll(serverTasks.map { it.toLocal() })
//        // ... другие сущности
//
//        // Обновляем время последней синхронизации
//        db.syncStatusDao().upsert(
//            SyncStatus(
//                tableName = "all",
//                lastSyncTime = System.currentTimeMillis(),
//                pendingChanges = false
//            )
//        )
//    }
//
//    // Вспомогательные функции для каждой сущности...
//}