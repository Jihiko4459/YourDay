package com.example.yourday.api

import co.touchlab.kermit.Logger
import com.example.yourday.model.BodyMeasurement
import com.example.yourday.model.DailyNote
import com.example.yourday.model.Goal
import com.example.yourday.model.GratitudeAndJoyJournal
import com.example.yourday.model.Idea
import com.example.yourday.model.LocalBodyMeasurement
import com.example.yourday.model.LocalChecklistItem
import com.example.yourday.model.LocalDailyNote
import com.example.yourday.model.LocalGoal
import com.example.yourday.model.LocalGratitudeAndJoyJournal
import com.example.yourday.model.LocalHabit
import com.example.yourday.model.LocalHobby
import com.example.yourday.model.LocalIdea
import com.example.yourday.model.LocalItemMark
import com.example.yourday.model.LocalMedication
import com.example.yourday.model.LocalNutritionLog
import com.example.yourday.model.LocalSteps
import com.example.yourday.model.LocalTask
import com.example.yourday.model.LocalTaskDependency
import com.example.yourday.model.LocalTransaction
import com.example.yourday.model.LocalUserActivity
import com.example.yourday.model.LocalUserArticleStatus
import com.example.yourday.model.LocalWaterIntake
import com.example.yourday.model.NutritionLog
import com.example.yourday.model.ProfileData
import com.example.yourday.model.Steps
import com.example.yourday.model.Task
import com.example.yourday.model.Transaction
import com.example.yourday.model.UserActivity
import com.example.yourday.model.WaterIntake
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random


class SupabaseHelper() {
    private val log = Logger.withTag("SupabaseAuth")


    @OptIn(SupabaseInternal::class)
    internal val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB2bG56eWdpZ2pubW1zbWtycHBzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MjQ4NTksImV4cCI6MjA1ODQwMDg1OX0.c1o7HtVS701K5r_LkuykOqcANhqeOF0546MY1GuErvQ"

        ) {
            install(Auth) {
                alwaysAutoRefresh = false // Лучше отключить автообновление
                autoLoadFromStorage = true // Включить автосохранение
            }
            install(Postgrest) {
                serializer = KotlinXSerializer()
            }
            // Добавляем настройки таймаута и ретраев
            httpConfig {
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000 // 30 секунд
                    connectTimeoutMillis = 30000
                    socketTimeoutMillis = 30000
                }
                install(HttpRequestRetry) {
                    retryOnException(maxRetries = 3)
                    exponentialDelay()
                }
            }




        }

    }

    suspend fun getCurrentSession(): UserSession? {
        return try {
            // Пытаемся получить текущую сессию
            var session = client.auth.currentSessionOrNull()

            // Если сессия есть, но истекла - пробуем обновить
            session?.let {
                if (Instant.parse(it.expiresAt.toString()) < Clock.System.now()) {
                    client.auth.refreshCurrentSession()
                    session = client.auth.currentSessionOrNull()
                }
            }

            session
        } catch (e: Exception) {
            log.e(e) { "Error getting current session" }
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
            if (!ensureAuthenticated()) {
                return Result.failure(Exception("User not authenticated"))
            }
            Result.success(block())
        } catch (e: Exception) {
            log.e(e) { "Error in authenticated operation" }
            Result.failure(e)
        }
    }


    fun checkSessionValidity() {
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

            // Получаем сессию
            val session = client.auth.currentSessionOrNull()
                ?: return AuthResult.Failure(Exception("Session not found after login"))

            client.auth.sessionManager.saveSession(session)

            AuthResult.Success(userId)
        } catch (e: Exception) {
            Logger.e("SupabaseAuth", e) { "Registration error" }
            AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
        }
    }

    suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            // Выполняем вход
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            // Получаем сессию
            val session = client.auth.currentSessionOrNull()
                ?: return AuthResult.Failure(Exception("Session not found after login"))

            client.auth.sessionManager.saveSession(session)
            // Получаем ID пользователя
            val userId = client.auth.currentUserOrNull()?.id
                ?: return AuthResult.Failure(Exception("User ID not found in session"))

            AuthResult.Success(userId, session)
        } catch (e: Exception) {
            AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
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


    //загрузка данных для главного экрана

    internal suspend fun getDailyNotes(date: String, userId: String): List<DailyNote> {
        return withAuth {
            client.postgrest.from("daily_notes")
                .select {
                    filter {
                        eq("date", date)
                        eq("user_id", userId)
                    }
                }.decodeList<DailyNote>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getGratitudeJournal(date: String, userId: String): List<GratitudeAndJoyJournal> {
        return withAuth {
            client.postgrest.from("gratitude_and_joy_journals")
                .select {
                    filter {
                        eq("date", date)
                        eq("user_id", userId)
                    }
                }
                .decodeList<GratitudeAndJoyJournal>()
        }.getOrElse { emptyList() }

    }

    internal suspend fun getDailyTasks(date: String, userId: String): List<Task> {
        return withAuth { client.postgrest.from("tasks")
            .select {
                filter {
                    lte("created_at", date)
                    gte("due_date", date)
                    eq("user_id", userId)
                    eq("is_completed", false)
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

    internal suspend fun getDailyFinances(date: String, userId: String): List<Transaction> {
        return withAuth { client.postgrest.from("transactions")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
                order("date", Order.DESCENDING)

            }
            .decodeList<Transaction>()
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

    internal suspend fun getWaterIntake(date: String, userId: String): List<WaterIntake> {
        return withAuth { client.postgrest.from("water_intake")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<WaterIntake>()
        }.getOrElse { emptyList() }
    }

    internal suspend fun getNutritionLogs(date: String, userId: String): List<NutritionLog> {
        return withAuth { client.postgrest.from("nutrition_logs")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<NutritionLog>()
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

    internal suspend fun getBodyMeasurements(date: String, userId: String): List<BodyMeasurement> {
        return withAuth { client.postgrest.from("body_measurements")
            .select {
                filter {
                    eq("date", date)
                    eq("user_id", userId)
                }
            }
            .decodeList<BodyMeasurement>()
        }.getOrElse { emptyList() }
    }


    // Body Measurements
    suspend fun upsertBodyMeasurement(measurement: LocalBodyMeasurement): Boolean {
        return try {
            client.from("body_measurements").upsert(measurement)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting body measurement" }
            false
        }
    }

    suspend fun deleteBodyMeasurement(measurementId: Int): Boolean {
        return try {
            client.from("body_measurements").delete {
                filter { eq("id", measurementId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting body measurement" }
            false
        }
    }

    // Checklist Items
    suspend fun upsertChecklistItem(item: LocalChecklistItem): Boolean {
        return try {
            client.from("checklist_items").upsert(item)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting checklist item" }
            false
        }
    }

    suspend fun deleteChecklistItem(itemId: Int): Boolean {
        return try {
            client.from("checklist_items").delete {
                filter { eq("id", itemId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting checklist item" }
            false
        }
    }

    // Daily Notes
    suspend fun upsertDailyNote(note: LocalDailyNote): Boolean {
        return try {
            client.from("daily_notes").upsert(note)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting daily note" }
            false
        }
    }

    suspend fun deleteDailyNote(noteId: Int): Boolean {
        return try {
            client.from("daily_notes").delete {
                filter { eq("id", noteId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting daily note" }
            false
        }
    }


    // Goals
    suspend fun upsertGoal(goal: LocalGoal): Boolean {
        return try {
            client.from("goals").upsert(goal)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting goal" }
            false
        }
    }

    suspend fun deleteGoal(goalId: Int): Boolean {
        return try {
            client.from("goals").delete {
                filter { eq("id", goalId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting goal" }
            false
        }
    }

    // Gratitude and Joy Journals
    suspend fun upsertGratitudeAndJoyJournal(journal: LocalGratitudeAndJoyJournal): Boolean {
        return try {
            client.from("gratitude_and_joy_journals").upsert(journal)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting gratitude and joy journal" }
            false
        }
    }

    suspend fun deleteGratitudeAndJoyJournal(journalId: Int): Boolean {
        return try {
            client.from("gratitude_and_joy_journals").delete {
                filter { eq("id", journalId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting gratitude and joy journal" }
            false
        }
    }

    // Habits
    suspend fun upsertHabit(habit: LocalHabit): Boolean {
        return try {
            client.from("habits").upsert(habit)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting habit" }
            false
        }
    }

    suspend fun deleteHabit(habitId: Int): Boolean {
        return try {
            client.from("habits").delete {
                filter { eq("id", habitId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting habit" }
            false
        }
    }

    // Hobbies
    suspend fun upsertHobby(hobby: LocalHobby): Boolean {
        return try {
            client.from("hobbies").upsert(hobby)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting hobby" }
            false
        }
    }

    suspend fun deleteHobby(hobbyId: Int): Boolean {
        return try {
            client.from("hobbies").delete {
                filter { eq("id", hobbyId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting hobby" }
            false
        }
    }

    // Ideas
    suspend fun upsertIdea(idea: LocalIdea): Boolean {
        return try {
            client.from("ideas").upsert(idea)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting idea" }
            false
        }
    }

    suspend fun deleteIdea(ideaId: Int): Boolean {
        return try {
            client.from("ideas").delete {
                filter { eq("id", ideaId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting idea" }
            false
        }
    }

    // Item Marks
    suspend fun upsertItemMark(mark: LocalItemMark): Boolean {
        return try {
            client.from("item_marks").upsert(mark)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting item mark" }
            false
        }
    }

    suspend fun deleteItemMark(markId: Int): Boolean {
        return try {
            client.from("item_marks").delete {
                filter { eq("id", markId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting item mark" }
            false
        }
    }

    // Medications
    suspend fun upsertMedication(medication: LocalMedication): Boolean {
        return try {
            client.from("medications").upsert(medication)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting medication" }
            false
        }
    }

    suspend fun deleteMedication(medicationId: Int): Boolean {
        return try {
            client.from("medications").delete {
                filter { eq("id", medicationId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting medication" }
            false
        }
    }



    // Nutrition Logs
    suspend fun upsertNutritionLog(log: LocalNutritionLog): Boolean {
        return try {
            client.from("nutrition_logs").upsert(log)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting nutrition log" }
            false
        }
    }

    suspend fun deleteNutritionLog(logId: Int): Boolean {
        return try {
            client.from("nutrition_logs").delete {
                filter { eq("id", logId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting nutrition log" }
            false
        }
    }

    // Steps
    suspend fun upsertSteps(steps: LocalSteps): Boolean {
        return try {
            client.from("steps").upsert(steps)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting steps" }
            false
        }
    }

    suspend fun deleteSteps(stepsId: Int): Boolean {
        return try {
            client.from("steps").delete {
                filter { eq("id", stepsId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting steps" }
            false
        }
    }

    // Task Dependencies
    suspend fun upsertTaskDependency(dependency: LocalTaskDependency): Boolean {
        return try {
            client.from("task_dependencies").upsert(dependency)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting task dependency" }
            false
        }
    }

    suspend fun deleteTaskDependency(dependencyId: Int): Boolean {
        return try {
            client.from("task_dependencies").delete {
                filter { eq("id", dependencyId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting task dependency" }
            false
        }
    }

    // Tasks
    suspend fun upsertTask(task: LocalTask): Boolean {
        return try {
            client.from("tasks").upsert(task)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting task" }
            false
        }
    }

    suspend fun deleteTask(taskId: Int): Boolean {
        return try {
            client.from("tasks").delete {
                filter { eq("id", taskId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting task" }
            false
        }
    }

    // Transactions
    suspend fun upsertTransaction(transaction: LocalTransaction): Boolean {
        return try {
            client.from("transactions").upsert(transaction)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting transaction" }
            false
        }
    }

    suspend fun deleteTransaction(transactionId: Int): Boolean {
        return try {
            client.from("transactions").delete {
                filter { eq("id", transactionId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting transaction" }
            false
        }
    }

    // User Activities
    suspend fun upsertUserActivity(activity: LocalUserActivity): Boolean {
        return try {
            client.from("user_activities").upsert(activity)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting user activity" }
            false
        }
    }

    suspend fun deleteUserActivity(activityId: Int): Boolean {
        return try {
            client.from("user_activities").delete {
                filter { eq("id", activityId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting user activity" }
            false
        }
    }

    // User Article Statuses
    suspend fun upsertUserArticleStatus(status: LocalUserArticleStatus): Boolean {
        return try {
            client.from("user_article_statuses").upsert(status)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting user article status" }
            false
        }
    }

    suspend fun deleteUserArticleStatus(statusId: Int): Boolean {
        return try {
            client.from("user_article_statuses").delete {
                filter { eq("id", statusId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting user article status" }
            false
        }
    }

    // Water Intake
    suspend fun upsertWaterIntake(intake: LocalWaterIntake): Boolean {
        return try {
            client.from("water_intake").upsert(intake)
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error upserting water intake" }
            false
        }
    }

    suspend fun deleteWaterIntake(intakeId: Int): Boolean {
        return try {
            client.from("water_intake").delete {
                filter { eq("id", intakeId) }
            }
            true
        } catch (e: Exception) {
            Logger.e(e) { "Error deleting water intake" }
            false
        }
    }





}

private data class FriendshipCode(val friendship_code: String)



@Serializable(with = LocalDateSerializer::class)
class LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}



