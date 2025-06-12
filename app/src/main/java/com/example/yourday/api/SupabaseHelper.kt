package com.example.yourday.api

import co.touchlab.kermit.Logger
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
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
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
            e.message?.contains("password", ignoreCase = true) == true ->
                "Пароль должен содержать минимум 6 символов"
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
}

private data class FriendshipCode(val friendship_code: String)

@Serializable
data class ProfileData(
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("birth_date") val birthDate: LocalDate,
    @SerialName("gender_id") val genderId: Int? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("friendship_code") val friendshipCode: String,
    @SerialName("registration_date") val registrationDate: String,
    @SerialName("last_login") val lastLogin: String,
    @SerialName("is_active") val isActive: Boolean
)

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

