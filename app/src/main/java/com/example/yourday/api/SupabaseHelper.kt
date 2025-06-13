package com.example.yourday.api

import co.touchlab.kermit.Logger
import com.example.yourday.models.BodyMeasurement
import com.example.yourday.models.DailyFinances
import com.example.yourday.models.DailyGoals
import com.example.yourday.models.DailyIdeas
import com.example.yourday.models.DailyNote
import com.example.yourday.models.DailyNotes
import com.example.yourday.models.DailyScreenData
import com.example.yourday.models.DailyTasks
import com.example.yourday.models.Goal
import com.example.yourday.models.GoalProgress
import com.example.yourday.models.GratitudeAndJoyJournal
import com.example.yourday.models.GratitudeJournal
import com.example.yourday.models.HealthData
import com.example.yourday.models.Idea
import com.example.yourday.models.NutritionLog
import com.example.yourday.models.ProfileData
import com.example.yourday.models.Steps
import com.example.yourday.models.Task
import com.example.yourday.models.Transaction
import com.example.yourday.models.UserActivity
import com.example.yourday.models.WaterIntake
import io.github.jan.supabase.SupabaseClient
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
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random


class SupabaseHelper() {
    private val log = Logger.withTag("SupabaseAuth")

    internal val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://pvlnzygigjnmmsmkrpps.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB2bG56eWdpZ2pubW1zbWtycHBzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI4MjQ4NTksImV4cCI6MjA1ODQwMDg1OX0.c1o7HtVS701K5r_LkuykOqcANhqeOF0546MY1GuErvQ"

        ) {
            install(Auth) {
                alwaysAutoRefresh = false // Отключаем автообновление
                autoLoadFromStorage = false // Отключаем автосохранение сессии
            }
            install(Postgrest) {
                serializer = KotlinXSerializer()
            }

        }

    }
    init {
        // Очищаем сессию при инициализации
        runBlocking {
            try {
                client.auth.signOut()
            } catch (e: Exception) {
                log.e(e) { "Failed to clear session" }
            }
        }
    }

    suspend fun safeSignOut() {
        try {
            // Получаем текущую сессию без проверки валидности
            val session = client.auth.currentSessionOrNull()
            if (session != null) {
                try {
                    // Пытаемся выйти стандартным способом
                    client.auth.signOut()
                } catch (e: Exception) {
                    if (e.message?.contains("User from sub claim in JWT does not exist") == true) {
                        // Если пользователь не существует, просто очищаем локальную сессию
                        log.w { "User session invalid, clearing local session" }
                        client.auth.clearSession()
                    } else {
                        log.e(e) { "Sign out failed" }
                    }
                }
            }
        } catch (e: Exception) {
            log.e(e) { "Error during sign out attempt" }
        }
    }


    suspend fun checkSessionValidity() {
        try {
            client.auth.currentSessionOrNull()?.let { session ->
                if (Instant.parse(session.expiresAt.toString()) < Clock.System.now()) {
                    log.d { "Session expired, signing out" }
                    safeSignOut()
                }
            }
        } catch (e: Exception) {
            log.e(e) { "Error checking session validity" }
            safeSignOut()
        }
    }

    sealed class AuthResult {
        data class Success(val userId: String, val session: UserSession?=null) : AuthResult()
        data class Failure(val error: Exception) : AuthResult()
    }

    suspend fun signUpWithEmail(email: String, password: String): AuthResult {

        return try {
            safeSignOut()

            // 1. Регистрируем пользователя
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val currentUser = client.auth.currentUserOrNull()
            val userId=currentUser?.id.toString()

            AuthResult.Success(userId)
        } catch (e: Exception) {
            Logger.e("SupabaseAuth", e) { "Registration error" }
            AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
        }
    }

    suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            safeSignOut()

            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val currentUser = client.auth.currentUserOrNull()
            val userId=currentUser?.id.toString()

            AuthResult.Success(userId)
        } catch (e: Exception) {
            Logger.e("SupabaseAuth", e) { "Login error" }
            AuthResult.Failure(Exception(mapErrorToUserMessage(e)))
        }
    }

    private fun mapErrorToUserMessage(e: Exception): String {
        return when {
            e.message?.contains("already registered", ignoreCase = true) == true ->
                "Email уже зарегистрирован"
            e.message?.contains("email", ignoreCase = true) == true ->
                "Неверный формат email"
            e is java.net.ConnectException ->
                "Нет соединения с сервером"
            e is java.net.SocketTimeoutException ->
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
                        this.filter{
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

    suspend fun getDailyData(date: String): DailyScreenData? = withContext(Dispatchers.IO) {
        return@withContext try {
            // Fetch all necessary data in parallel
            val notes = getDailyNotes(date)
            val gratitude = getGratitudeJournal(date)
            val tasks = getDailyTasks(date)
            val goals = getDailyGoals(date)
            val ideas = getDailyIdeas(date)
            val finances = getDailyFinances(date)

            // Transform to DailyScreenData
            DailyScreenData(
                date = date,
                tasks = DailyTasks(
                    count = tasks.size,
                    items = tasks,
                    recommendation = "Plan your day effectively",
                    emergency = "No emergencies"
                ),
                gratitudeJournal = GratitudeJournal(
                    gratitudeItems = gratitude.mapNotNull { it.gratitude },
                    joyItems = gratitude.mapNotNull { it.joy }
                ),
                notes = DailyNotes(
                    items = notes.map {
                        DailyNote(
                            id = it.id,
                            userId = it.userId,
                            date = it.date.toString(),
                            note = it.note ?: ""
                        )
                    }
                ),
                goals = DailyGoals(
                    activeGoals = goals.map { goal ->
                        GoalProgress(
                            goal = Goal(
                                id = goal.id,
                                userId = goal.userId,
                                title = goal.title,
                                description = goal.description,
                                goalTypeId = goal.goalTypeId,
                                statusId = goal.statusId,
                                progressGoal = goal.progressGoal ?: 0.0, // Handle nullable Double
                                createdAt = goal.createdAt,
                                achievedAt = goal.achievedAt,
                                lastUpdated = goal.lastUpdated
                            ),
                            progress = goal.progressGoal ?: 0.0 // Use actual progress from goal
                        )
                    }
                ),
                ideas = DailyIdeas(
                    items = ideas.map { idea ->
                        Idea(
                            id = idea.id,
                            userId = idea.userId,
                            date = idea.date, // Assuming idea.date is already String or convertible
                            title = idea.title,
                            description = idea.description
                        )
                    }
                ),
                finances = DailyFinances(
                    balance = finances.sumOf { it.amount },
                    income = finances.filter { it.amount > 0 }.sumOf { it.amount },
                    expenses = finances.filter { it.amount < 0 }.sumOf { it.amount },
                    lastTransaction = finances.maxByOrNull { it.date }?.let {
                        Transaction(
                            id = 0, // or get actual id if available
                            userId = "", // or get actual userId
                            title = it.title ?: "",
                            description = it.description,
                            balanceTypeId = null,
                            amount = it.amount,
                            date = it.date.toString()
                        )
                    }
                )
            )
        } catch (e: Exception) {
            Logger.e(e) { "Error fetching daily data" }
            null
        }
    }

    // For daily_notes table (assuming it's in the public schema)
    private suspend fun getDailyNotes(date: String): List<DailyNote> {
        return client.postgrest.from("daily.daily_notes")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getGratitudeJournal(date: String): List<GratitudeAndJoyJournal> {
        return client.postgrest.from("daily.gratitude_and_joy_journals")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getDailyTasks(date: String): List<Task> {
        return client.postgrest.from("tasks.tasks")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getDailyGoals(date: String): List<Goal> {
        return client.postgrest.from("goals_and_habits.goals")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getDailyIdeas(date: String): List<Idea> {
        return client.postgrest.from("daily.ideas")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getDailyFinances(date: String): List<Transaction> {
        return client.postgrest.from("finance.transactions")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    suspend fun getHealthDataForDate(date: String): HealthData {
        return try {
            val steps = getStepsData(date)
            val water = getWaterIntake(date)
            val nutrition = emptyList<NutritionLog>() // Add proper nutrition data if available
            val activity = getUserActivity(date)
            val measurements = getBodyMeasurements(date)

            HealthData(steps, water, nutrition, activity, measurements)
        } catch (e: Exception) {
            Logger.e(e) { "Error fetching health data" }
            HealthData(emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
        }
    }


    // For health_and_fitness tables (assuming they're in the public schema)
    private suspend fun getStepsData(date: String): List<Steps> {
        return client.postgrest.from("health_and_fitness.steps")  // Remove "health_and_fitness." prefix
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getWaterIntake(date: String): List<WaterIntake> {
        return client.postgrest.from("health_and_fitness.water_intake")  // Remove "health_and_fitness." prefix
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getUserActivity(date: String): List<UserActivity> {
        return client.postgrest.from("health_and_fitness.user_activity")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
    }

    private suspend fun getBodyMeasurements(date: String): List<BodyMeasurement> {
        return client.postgrest.from("health_and_fitness.body_measurements")
            .select {
                filter { eq("date", date) }
            }
            .decodeList()
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

