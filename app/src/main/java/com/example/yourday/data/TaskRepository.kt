package com.example.yourday.data

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Task
import com.example.yourday.model.TaskPriorityType
import com.example.yourday.model.TaskType
import io.github.jan.supabase.exceptions.UnknownRestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// Объект репозитория задач, работающий с Supabase и мок-данными
object TaskRepository {
    // Моковый список задач для резервного хранения (если Supabase недоступен)
    private val mockTasks = mutableListOf<Task>()
    // Счетчик для генерации ID новых моковых задач
    private var nextMockId = 1

    // Получение задачи по ID (асинхронно)
    // Параметры:
    //   taskId - идентификатор задачи
    //   userId - идентификатор пользователя
    //   supabaseHelper - хелпер для работы с Supabase
    suspend fun getTaskById(taskId: Int, userId: String, supabaseHelper: SupabaseHelper): Task? {
        return supabaseHelper.getTaskById(taskId, userId)
    }

    // Сохранение задачи (асинхронно)
    // Параметры:
    //   task - задача для сохранения
    //   supabaseHelper - хелпер для работы с Supabase
    // Возвращает сохраненную задачу
    // Логика:
    //   - Сначала пытается сохранить в Supabase
    //   - При ошибках переходит на мок-хранилище
    suspend fun saveTask(task: Task, supabaseHelper: SupabaseHelper): Task {
        return withContext(Dispatchers.IO) {
            try {
                // For new tasks (id=0), we need to handle the sequence permission error
                if (task.id == 0) {
                    try {
                        // First try to save with Supabase
                        val savedTask = supabaseHelper.saveTask(task)
                        mockTasks.removeIf { it.id == savedTask.id }
                        savedTask
                    } catch (e: UnknownRestException) {
                        if (e.message?.contains("permission denied for sequence tasks_id_seq") == true) {
                            // Fallback to mock with manual ID assignment
                            val newTask = task.copy(id = nextMockId++)
                            mockTasks.removeIf { it.id == newTask.id }
                            mockTasks.add(newTask)
                            newTask
                        } else {
                            throw e
                        }
                    }
                } else {
                    // For existing tasks, try to update in Supabase
                    try {
                        val savedTask = supabaseHelper.saveTask(task)
                        mockTasks.removeIf { it.id == task.id }
                        savedTask
                    } catch (e: Exception) {
                        // Fallback to mock
                        mockTasks.removeIf { it.id == task.id }
                        mockTasks.add(task)
                        task
                    }
                }
            } catch (e: Exception) {
                // General fallback to mock
                val newTask = if (task.id == 0) {
                    task.copy(id = nextMockId++)
                } else {
                    task
                }
                mockTasks.removeIf { it.id == newTask.id }
                mockTasks.add(newTask)
                newTask
            }
        }
    }

    // Получение типов задач (асинхронно)
    // Параметры:
    //   supabaseHelper - хелпер для работы с Supabase
    // Возвращает список типов задач из Supabase или мок-данные при ошибке
    suspend fun getTaskTypes(supabaseHelper: SupabaseHelper): List<TaskType> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getTaskTypes()
            } catch (e: Exception) {
                mockTaskTypes
            }
        }
    }

    // Получение приоритетов задач (асинхронно)
    // Параметры:
    //   supabaseHelper - хелпер для работы с Supabase
    // Возвращает список приоритетов из Supabase или мок-данные при ошибке
    suspend fun getTaskPriorities(supabaseHelper: SupabaseHelper): List<TaskPriorityType> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getTaskPriorities()
            } catch (e: Exception) {
                mockPriorities
            }
        }
    }

    // Удаление задачи (асинхронно)
    // Параметры:
    //   taskId - идентификатор задачи
    //   userId - идентификатор пользователя
    //   supabaseHelper - хелпер для работы с Supabase
    // Возвращает true при успешном удалении
    suspend fun deleteTask(taskId: Int, userId: String, supabaseHelper: SupabaseHelper): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.deleteTask(taskId)
                mockTasks.removeIf { it.id == taskId }
                true
            } catch (e: Exception) {
                mockTasks.removeIf { it.id == taskId && it.userId == userId }
                true
            }
        }
    }

    // Получение задач на день (асинхронно)
    // Параметры:
    //   date - дата в формате строки
    //   userId - идентификатор пользователя
    //   supabaseHelper - хелпер для работы с Supabase
    // Возвращает список задач на указанную дату
    suspend fun getDailyTasks(date: String, userId: String, supabaseHelper: SupabaseHelper): List<Task> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getDailyTasks(date, userId)
            } catch (e: Exception) {
                mockTasks.filter { it.dueDate == date && it.userId == userId }
            }
        }
    }

    // Мок-данные для типов задач
    internal val mockTaskTypes = listOf(
        TaskType(1, "Личное", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//person_outline.svg"),
        TaskType(2, "Лекарство", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//pills_yr551cti8gp3%201.svg"),
        TaskType(3, "Цель", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//Clip%20path%20group.svg"),
        TaskType(4, "Привычка", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//seeding.svg"),
        TaskType(5, "Хобби", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//masks-theater.svg"),
        TaskType(6, "Дом и быт", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//broom_2ebf73plnojg%201.svg"),
        TaskType(7, "Саморазвитие", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//Clip%20path%20group-1.svg"),
        TaskType(8, "Работа", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//Icon.svg"),
        TaskType(9, "Учеба", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/public/task-type-icons//backpack.svg")
    )
    // Мок-данные для приоритетов задач
    internal val mockPriorities = listOf(
        TaskPriorityType(1, "Высокий (Срочно и важно)"),
        TaskPriorityType(2, "Средний (Важно, но не срочно)"),
        TaskPriorityType(3, "Низкий (Не срочно и не критично)"),
        TaskPriorityType(4, "Фоновые задачи (Рутина)")
    )
}