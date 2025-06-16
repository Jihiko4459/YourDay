package com.example.yourday.viewmodel.goals_and_habits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalGoalAndHabitType
import com.example.yourday.repository.goals_and_habits.GoalAndHabitTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GoalAndHabitTypeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GoalAndHabitTypeRepository
    val types: MutableStateFlow<List<LocalGoalAndHabitType>> = MutableStateFlow(emptyList())

    init {
        val db = YourDayDatabase.getDatabase(application)
        repository = GoalAndHabitTypeRepository(db)
        loadTypes()
    }

    private fun loadTypes() {
        viewModelScope.launch {
            repository.getAll().collect { typesList ->
                types.value = typesList
            }
        }
    }

    fun upsertType(type: LocalGoalAndHabitType) {
        viewModelScope.launch {
            repository.upsert(type)
        }
    }

    fun deleteType(type: LocalGoalAndHabitType) {
        viewModelScope.launch {
            repository.delete(type)
        }
    }
    fun insertAllReferenceTypes() {
        viewModelScope.launch {
            val referenceTypes = listOf(
                LocalGoalAndHabitType(1, "Книги", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS5zdmciLCJpYXQiOjE3NDkyNzY0NzYsImV4cCI6MTc4MDgxMjQ3Nn0.s4qKaDsxr7Zhb2Ep6bp15Nqfr5-ubXIjIoB8c4dzTPg"),
                LocalGoalAndHabitType(2, "Здоровье", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/healthy-recognition.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9oZWFsdGh5LXJlY29nbml0aW9uLnN2ZyIsImlhdCI6MTc0OTI3NjQ5MiwiZXhwIjoxNzgwODEyNDkyfQ.yiMmzxIG5sIUSlvNLcTIWETWx9welQKTQliaPKeMEps"),
                LocalGoalAndHabitType(3, "Обучение", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Education.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9FZHVjYXRpb24uc3ZnIiwiaWF0IjoxNzQ5Mjc2NDk5LCJleHAiOjE3ODA4MTI0OTl9.YkSQp2Ns7qP1IoadtytUP7uLHMcaXVLrgnWXsLMNA-M"),
                LocalGoalAndHabitType(4, "Творчество", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/masks-theater.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9tYXNrcy10aGVhdGVyLnN2ZyIsImlhdCI6MTc0OTI3NjUxMSwiZXhwIjoxNzgwODEyNTExfQ.2cQzTRh84_0xM8b10ZEYG7nAC89nRDYMjJDdbVvXjAc"),
                LocalGoalAndHabitType(5, "Деньги", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-1.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS0xLnN2ZyIsImlhdCI6MTc0OTI3NjUyMCwiZXhwIjoxNzgwODEyNTIwfQ.1EGS1N_en2U99wtCgAuRfz7gHUfhq8ijQOF1hxKxgAo"),
                LocalGoalAndHabitType(6, "Работа", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/briefcase.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9icmllZmNhc2Uuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTM0LCJleHAiOjE3ODA4MTI1MzR9.flxtO-uGOO0nml32ee4z7CLJzc2TewkUWeEuuf-pEBc"),
                LocalGoalAndHabitType(7, "Семья", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/family.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9mYW1pbHkuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQxLCJleHAiOjE3ODA4MTI1NDF9.xi09ZYTFsOSkgx7ZpXNGzLjjbeQDKsNK3nFXENWAdC8"),
                LocalGoalAndHabitType(8, "Общение", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/chatbubble-ellipses-outline.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9jaGF0YnViYmxlLWVsbGlwc2VzLW91dGxpbmUuc3ZnIiwiaWF0IjoxNzQ5Mjc2NTQ5LCJleHAiOjE3ODA4MTI1NDl9.IawzsQY9jpSUQdHg6HL1f0USPkTEV5YgonJeMux6TYs"),
                LocalGoalAndHabitType(9, "Личные вызовы", "https://pvlnzygigjnmmsmkrpps.supabase.co/storage/v1/object/sign/goals-and-habit-type-icons/Frame-2.svg?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV9hOWEyZDYxNC0zYWFmLTRkYzQtYjUyMC0yMWU3MDIzNjRkNDkiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJnb2Fscy1hbmQtaGFiaXQtdHlwZS1pY29ucy9GcmFtZS0yLnN2ZyIsImlhdCI6MTc0OTI3NjU1NCwiZXhwIjoxNzgwODEyNTU0fQ.1A3wWlbqU3N-lT1OHjeRu-vemXOUX3AjOWyXrKf-zwE")
            )
            repository.insertAllReferenceTypes(referenceTypes)
        }
    }

    // Функция для удаления всех справочных типов
    fun deleteAllReferenceTypes() {
        viewModelScope.launch {
            repository.deleteAllReferenceTypes()
        }
    }
}