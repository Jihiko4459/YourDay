package com.example.yourday.api

import android.content.Context
import android.net.ConnectivityManager
import co.touchlab.kermit.Logger
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.model.ArticleInCategory
import com.example.yourday.model.Goal
import com.example.yourday.model.Idea
import com.example.yourday.model.ProfileData
import com.example.yourday.model.Steps
import com.example.yourday.model.Task
import com.example.yourday.model.TaskPriorityType
import com.example.yourday.model.TaskType
import com.example.yourday.model.UserActivity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random


class SupabaseHelper(private val context: Context) {
    private val log = Logger.withTag("SupabaseAuth")

    internal val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB2bG56eWdpZ2pubW1zbWtycHBzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MjQ4NTksImV4cCI6MjA1ODQwMDg1OX0.c1o7HtVS701K5r_LkuykOqcANhqeOF0546MY1GuErvQ"

        ) {

            install(Auth) {
                alwaysAutoRefresh = true // Лучше отключить автообновление
                autoLoadFromStorage = true // Включить автосохранение
            }
            install(Postgrest) {
                serializer = KotlinXSerializer(json = Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
                }
        }

    }

    suspend fun getCurrentSession(): UserSession? {
        return try {
            // Получаем текущую сессию (или null)
            var session = client.auth.currentSessionOrNull() ?: return null

            // Если сессия истекла, пытаемся обновить
            if (Instant.parse(session.expiresAt.toString()) < Clock.System.now()) {
                try {
                    // Обновляем сессию (это сохраняет её внутри клиента)
                    client.auth.refreshCurrentSession() // ⚠️ Возвращает Unit

                    // Получаем обновлённую сессию
                    session = client.auth.currentSessionOrNull() ?: return null

                    // Явно сохраняем (если нужно)
                    client.auth.sessionManager.saveSession(session)
                } catch (e: Exception) {
                    log.e(e) { "Failed to refresh session" }
                    // Пробуем загрузить из хранилища
                    client.auth.loadFromStorage()
                    session = client.auth.currentSessionOrNull() ?: return null
                }
            }

            session // Возвращаем актуальную сессию (или null)
        } catch (e: Exception) {
            log.e(e) { "Error in getCurrentSession()" }
            null
        }
    }

    suspend fun ensureAuthenticated(): Boolean {
        return try {
            // Пытаемся получить сессию
            val session = getCurrentSession()

            if (session == null) {
                // Пробуем загрузить из хранилища
                client.auth.loadFromStorage()
                return client.auth.currentSessionOrNull() != null
            }

            true
        } catch (e: Exception) {
            log.e(e) { "Authentication check failed" }
            false
        }
    }

    internal suspend fun <T> withAuth(block: suspend () -> T): Result<T> {
        return try {
            // 1. Проверяем соединение с интернетом
            if (!isNetworkAvailable()) {
                return Result.failure(Exception("No internet connection"))
            }

            // 2. Получаем сессию с увеличенным таймаутом
            val session = getCurrentSession() ?: return Result.failure(Exception("Not authenticated"))

            // 3. Выполняем операцию
            Result.success(block())
        } catch (e: Exception) {
            when (e) {
                is UnauthorizedRestException -> {
                    Result.failure(Exception("Authentication required"))
                }
                is HttpRequestTimeoutException -> {
                    Result.failure(Exception("Request timeout. Please try again"))
                }
                else -> {
                    Result.failure(Exception("Network error: ${e.message}"))
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    suspend fun checkSessionValidity() {
        try {
            client.auth.currentSessionOrNull()?.let { session ->
                if (Instant.parse(session.expiresAt.toString()) < Clock.System.now()) {
                    log.d { "Session expired, signing out" }
                }
            }
        } catch (e: Exception) {
            log.e(e) { "Error checking session validity" }
        }
    }

    sealed class AuthResult {
        data class Success(val userId: String, val session: UserSession? = null) : AuthResult()
        data class Failure(val error: Exception) : AuthResult()
    }

    suspend fun signUpWithEmail(email: String, password: String): AuthResult {

        return try {

            // 1. Регистрируем пользователя
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val currentUser = client.auth.currentUserOrNull()
            val userId = currentUser?.id.toString()

            AuthResult.Success(userId)
        } catch (e: Exception) {
            Logger.e("SupabaseAuth", e) { "Registration error" }
            AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
        }
    }

    suspend fun <T> withRetry(
        maxRetries: Int = 3,
        initialDelay: Long = 1000,
        maxDelay: Long = 10000,
        block: suspend () -> T
    ): Result<T> {
        var currentRetry = 0
        var delay = initialDelay

        while (currentRetry < maxRetries) {
            try {
                return Result.success(block())
            } catch (e: Exception) {
                currentRetry++
                if (currentRetry == maxRetries) {
                    return Result.failure(e)
                }

                // Экспоненциальная задержка с ограничением
                delay = minOf(delay * 2, maxDelay)
                delay(delay)
            }
        }

        return Result.failure(Exception("Max retries exceeded"))
    }

    suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return withRetry {
            try {
                // Выполняем вход с увеличенным таймаутом
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val session = client.auth.currentSessionOrNull()
                val userId = client.auth.currentUserOrNull()?.id

                if (session == null || userId == null) {
                    AuthResult.Failure(Exception("Session not found after login"))
                } else {
                    AuthResult.Success(userId = userId, session = session)
                }
            } catch (e: Exception) {
                AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
            }
        }.getOrElse {
            AuthResult.Failure(Exception("Failed after retries: ${it.message}"))
        }
    }



    private fun mapErrorToUserMessage(e: Exception): String {
        return when {
            e.message?.contains("already registered", ignoreCase = true) == true ->
                "Email уже зарегистрирован"

            e.message?.contains("email", ignoreCase = true) == true ->
                "Неверный формат email"

            e is ConnectException ->
                "Нет соединения с сервером"

            e is SocketTimeoutException ->
                "Таймаут соединения"

            e.message?.contains("Invalid login credentials") == true ->
                "Неверный email или пароль"

            e is BadRequestRestException ->
                "Некорректный запрос к серверу"

            e is HttpRequestTimeoutException ->
                "Превышено время ожидания ответа"

            else ->
                "Ошибка: ${e.message ?: "Попробуйте позже"}"
        }
    }


    suspend fun insertUserProfile(
        userId: String,
        username: String,
        birthDate: String,
        genderId: Int? = null,
        avatarUrl: String? = null
    ): Boolean {
        return try {
            val day = birthDate.substring(0, 2).toInt()
            val month = birthDate.substring(2, 4).toInt()
            val year = birthDate.substring(4).toInt()
            val parsedDate = LocalDate(year, month, day)

            val profileData = ProfileData(
                userId = userId,
                username = username,
                birthDate = parsedDate,
                genderId = genderId,
                avatarUrl = avatarUrl,
                friendshipCode = generateUniqueFriendshipCode(),
                registrationDate = getCurrentDateTime(),
                lastLogin = getCurrentDateTime(),
                isActive = true
            )

            client.from("profiles").insert(profileData)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error registration profile" }
            false
        }
    }

    suspend fun updateUserProfile(
        userId: String,
        username: String? = null,
        birthDate: String? = null,
        genderId: Int? = null,
        avatarUrl: String? = null
    ): Boolean {
        return try {
            // Подготавливаем данные для обновления
            val updates = mutableMapOf<String, Any?>()

            username?.let { updates["username"] = it }
            avatarUrl?.let { updates["avatar_url"] = it }
            genderId?.let { updates["gender_id"] = it }

            birthDate?.let {
                val day = it.substring(0, 2).toInt()
                val month = it.substring(2, 4).toInt()
                val year = it.substring(4).toInt()
                updates["birth_date"] = LocalDate(year, month, day).toString()
            }

            // Добавляем обновление времени последнего входа
            updates["last_login"] = getCurrentDateTime()

            // Удаляем null значения и преобразуем оставшиеся к строковому представлению
            val nonNullUpdates = updates
                .filterValues { it != null }
                .mapValues { (_, value) -> value.toString() }

            // Выполняем обновление только если есть что обновлять
            if (nonNullUpdates.isNotEmpty()) {
                client.from("profiles")
                    .update(nonNullUpdates) {
                        filter {
                            eq("user_id", userId)
                        }
                    }
            }

            true
        } catch (e: Exception) {
            Logger.e(e) { "Error updating profile for user $userId" }
            false
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(Date())
    }

    private suspend fun generateUniqueFriendshipCode(): String {
        while (true) {
            val code = Random.nextInt(100000000, 999999999).toString()
            val exists = try {
                client.postgrest.from("profiles")
                    .select(columns = Columns.list("friendship_code")) {
                        this.filter {
                            eq("friendship_code", code)
                        }
                    }
                    .decodeList<FriendshipCode>().isNotEmpty()
            } catch (e: Exception) {
                false
            }
            if (!exists) return code
        }
    }



    internal suspend fun getDailyTasks(date: String, userId: String): List<Task> {
        return withAuth { client.postgrest.from("tasks")
            .select {
                filter {
                    lte("created_at", date)
                    gte("due_date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<Task>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getDailyGoals(date: String, userId: String): List<Goal> {
        return withAuth { client.postgrest.from("goals")
            .select {
                filter {
                    lte("created_at", date)
                    neq("status_id", 3) // Not completed
                    eq("user_id", userId)
                }
            }
            .decodeList<Goal>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getDailyIdeas(date: String, userId: String): List<Idea> {
        return withAuth { client.postgrest.from("ideas")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<Idea>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getStepsData(date: String, userId: String): List<Steps> {
        return withAuth { client.postgrest.from("steps")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<Steps>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getUserActivity(date: String, userId: String): List<UserActivity> {
        return withAuth { client.postgrest.from("user_activity")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<UserActivity>()
        }.getOrElse { emptyList() }
    }

    suspend fun getArticleCategories(): List<ArticleCategory> {
        return withAuth {
            try {
                println("Fetching article categories from Supabase...")
                val result = client.postgrest.from("article_categories")
                    .select()
                    .decodeList<ArticleCategory>()
                println("Successfully fetched ${result.size} categories")
                result
            } catch (e: Exception) {
                println("Error fetching categories: ${e.message}")
                throw e
            }
        }.getOrElse { emptyList() }
    }

    suspend fun getArticleInCategories(): List<ArticleInCategory> {
        return withAuth {
            try {
                println("Fetching article in categories from Supabase...")
                val result = client.postgrest.from("articles_in_categories")
                    .select()
                    .decodeList<ArticleInCategory>()
                println("Successfully fetched ${result.size} article-category relations")
                result
            } catch (e: Exception) {
                println("Error fetching article-category relations: ${e.message}")
                throw e
            }
        }.getOrElse { emptyList() }
    }

    suspend fun getArticles(): List<Article> {
        return withAuth {
            try {
                println("Fetching articles from Supabase...")
                val result = client.postgrest.from("articles")
                    .select()
                    .decodeList<Article>()
                println("Successfully fetched ${result.size} articles")
                result
            } catch (e: Exception) {
                println("Error fetching articles: ${e.message}")
                throw e
            }
        }.getOrElse { emptyList() }
    }
    suspend fun getArticleById(articleId: Int): Article {
        return withAuth {
            try {
                println("Fetching article with ID $articleId from Supabase...")
                val result = client.postgrest.from("articles")
                    .select {
                        filter {
                            eq("id", articleId)
                        }
                    }
                    .decodeList<Article>()

                if (result.isEmpty()) {
                    throw NoSuchElementException("Article with ID $articleId not found")
                }

                println("Successfully fetched article: ${result.first().title}")
                result.first()
            } catch (e: Exception) {
                println("Error fetching article with ID $articleId: ${e.message}")
                throw e
            }
        }.getOrThrow()
    }

    // First, let's add these functions to SupabaseHelper for task operations
    suspend fun getTaskById(taskId: Int, userId: String): Task {
        return withAuth {
            client.postgrest.from("tasks")
                .select {
                    filter {
                        eq("id", taskId)
                        eq("user_id", userId)
                    }
                }
                .decodeSingle<Task>()
        }.getOrThrow()
    }

    suspend fun saveTask(task: Task): Task {
        return withAuth {
            log.d { "Attempting to save task: $task" }

            val userId = client.auth.currentUserOrNull()?.id
                ?: throw Exception("User not authenticated").also {
                    log.e { "User not authenticated when saving task" }
                }

            // Ensure all date fields are properly formatted
            val taskToSave = task.copy(
                userId = userId,
                createdAt = if (task.createdAt.isBlank()) getCurrentDate() else task.createdAt,
                dueDate = if (task.dueDate.isBlank()) getCurrentDate() else task.dueDate,
                completedAt = task.completedAt.takeIf { !it.isNullOrBlank() } ?: ""
            )

            log.d { "Final task to save: $taskToSave" }

            try {
                val result = if (task.id == 0) {
                    log.d { "Inserting new task" }
                    client.postgrest.from("tasks")
                        .insert(taskToSave) {
                            select() // Return the inserted record
                        }
                        .decodeSingle<Task>()
                } else {
                    log.d { "Updating existing task ID ${task.id}" }
                    client.postgrest.from("tasks")
                        .update({
                            mapOf(
                                "title" to taskToSave.title,
                                "description" to taskToSave.description,
                                "due_date" to taskToSave.dueDate,
                                "created_at" to taskToSave.createdAt,
                                "completed_at" to taskToSave.completedAt,
                                "is_completed" to taskToSave.isCompleted,
                                "is_dependent" to taskToSave.isDependent,
                                "user_id" to taskToSave.userId,
                                "task_type_id" to taskToSave.taskTypeId,
                                "priority_id" to taskToSave.priorityId
                            ).filterValues { it != null }
                        }) {
                            filter {
                                eq("id", task.id)
                                eq("user_id", userId)
                            }
                            select() // Return the updated record
                        }
                        .decodeSingle<Task>()
                }
                log.d { "Task saved successfully: $result" }
                result
            } catch (e: Exception) {
                log.e(e) { "Error saving task" }
                throw Exception("Failed to save task: ${e.message}", e)
            }
        }.getOrElse {
            log.e(it) { "Failed to save task in withAuth block" }
            throw it
        }
    }

    suspend fun updateTask(
        taskId: Int,
        isCompleted: Boolean? = null,
        completedAt: String? = null,
        title: String? = null,
        description: String? = null
    ): Boolean {
        return try {
            val updates = mutableMapOf<String, String?>()

            isCompleted?.let { updates["is_completed"] = it.toString() }
            completedAt?.let { updates["completed_at"] = it }
            title?.let { updates["title"] = it }
            description?.let { updates["description"] = it }

            // Filter out null values
            val nonNullUpdates = updates.filterValues { it != null }

            if (nonNullUpdates.isNotEmpty()) {
                client.from("tasks")
                    .update(nonNullUpdates) {
                        filter {
                            eq("id", taskId)
                        }
                    }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            log.e(e) { "Error updating task with ID $taskId" }
            false
        }
    }

    // Helper function to get current date in correct format
    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun deleteTask(taskId: Int): Boolean {
        return withAuth {
            val userId = client.auth.currentUserOrNull()?.id
                ?: throw Exception("User not authenticated")

            client.postgrest.from("tasks")
                .delete {
                    filter {
                        eq("id", taskId)
                        eq("user_id", userId) // Ensure we only delete user's own tasks
                    }
                }
            true
        }.getOrThrow()
    }

    suspend fun getTaskTypes(): List<TaskType> {
        return withAuth {
            client.postgrest.from("task_types")
                .select()
                .decodeList<TaskType>()
        }.getOrElse { emptyList() }
    }

    suspend fun getTaskPriorities(): List<TaskPriorityType> {
        return withAuth {
            client.postgrest.from("task_priorite_types")
                .select()
                .decodeList<TaskPriorityType>()
        }.getOrElse { emptyList() }
    }

}

private data class FriendshipCode(val friendship_code: String)



object LocalDateSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        try {
            // Validate the date format
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(value)
            encoder.encodeString(value)
        } catch (e: Exception) {
            // Fallback to current date if invalid
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            encoder.encodeString(currentDate)
        }
    }

    override fun deserialize(decoder: Decoder): String {
        return try {
            decoder.decodeString()
        } catch (e: Exception) {
            // Return empty string if null or invalid
            ""
        }
    }
}

