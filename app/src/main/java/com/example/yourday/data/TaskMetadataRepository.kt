package com.example.yourday.data

import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.TaskPriorityType
import com.example.yourday.model.TaskType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TaskMetadataRepository {
    // Mock data for task types and priorities
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

    internal val mockPriorities = listOf(
        TaskPriorityType(1, "Высокий (Срочно и важно)"),
        TaskPriorityType(2, "Средний (Важно, но не срочно)"),
        TaskPriorityType(3, "Низкий (Не срочно и не критично)"),
        TaskPriorityType(4, "Фоновые задачи (Рутина)")
    )

    suspend fun getTaskTypes(supabaseHelper: SupabaseHelper): List<TaskType> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getTaskTypes().ifEmpty { mockTaskTypes }
            } catch (e: Exception) {
                mockTaskTypes
            }
        }
    }

    suspend fun getTaskPriorities(supabaseHelper: SupabaseHelper): List<TaskPriorityType> {
        return withContext(Dispatchers.IO) {
            try {
                supabaseHelper.getTaskPriorities().ifEmpty { mockPriorities }
            } catch (e: Exception) {
                mockPriorities
            }
        }
    }
}