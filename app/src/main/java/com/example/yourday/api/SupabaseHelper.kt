package com.example.yourday.api

import co.touchlab.kermit.Logger
import com.example.yourday.model.Article
import com.example.yourday.model.ArticleCategory
import com.example.yourday.model.ArticleInCategory
import com.example.yourday.model.BodyMeasurement
import com.example.yourday.model.DailyNote
import com.example.yourday.model.Goal
import com.example.yourday.model.GratitudeAndJoyJournal
import com.example.yourday.model.Idea
import com.example.yourday.model.MotivationalCard
import com.example.yourday.model.MotivationalUserCard
import com.example.yourday.model.NutritionLog
import com.example.yourday.model.ProfileData
import com.example.yourday.model.Steps
import com.example.yourday.model.Task
import com.example.yourday.model.Transaction
import com.example.yourday.model.UserActivity
import com.example.yourday.model.WaterIntake
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
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.ktor.client.plugins.HttpRequestTimeoutException
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
                serializer = KotlinXSerializer()

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
            val session = getCurrentSession()
            if (session == null) {
                return Result.failure(Exception("User not authenticated"))
            }
            Result.success(block())
        } catch (e: Exception) {
            when (e) {
                is UnauthorizedRestException -> {
                    // Попытка обновить сессию
                    try {
                        client.auth.refreshCurrentSession()
                        Result.success(block())
                    } catch (refreshEx: Exception) {
                        log.e(refreshEx) { "Error refreshing session" }
                        Result.failure(refreshEx)
                    }
                }
                else -> {
                    log.e(e) { "Error in authenticated operation" }
                    Result.failure(e)
                }
            }
        }
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

    // Получение случайной мотивационной карточки
    suspend fun getRandomMotivationalCard(): MotivationalCard {
        return withAuth {
            // Получаем все мотивационные карточки
            val allCards = client.postgrest.from("motivational_cards")
                .select()
                .decodeList<MotivationalCard>()

            // Выбираем случайную карточку
            allCards.random()
        }.getOrThrow()
    }

    // Добавим метод для получения карточки по ID
    suspend fun getMotivationalCardById(cardId: Int): MotivationalCard {
        return withAuth {
            client.postgrest.from("motivational_cards")
                .select {
                    filter { eq("id", cardId) }
                }
                .decodeSingle<MotivationalCard>()
        }.getOrThrow()
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
                val result = client.postgrest.from("article_in_categories")
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

    suspend fun saveMotivationalUserCard(userCard: MotivationalUserCard): Boolean {
        return try {
            // First check authentication
            if (!ensureAuthenticated()) {
                throw Exception("User not authenticated")
            }

            // Use withAuth for better error handling
            withAuth {
                client.postgrest.from("motivational_user_cards")
                    .insert(userCard)
                true
            }.getOrElse {
                Logger.e(it) { "Failed to save motivational user card" }
                false
            }
        } catch (e: Exception) {
            Logger.e(e) { "Error saving motivational user card: ${e.message}" }
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


