package com.example.yourday.screens



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.yourday.DateTransformation
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.model.LocalTask
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    database: YourDayDatabase, // Добавьте этот параметр
    onTaskCreated: () -> Unit = {}, // Колбек для уведомления о создании
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(
            context,//передаем контекст приложения
            YourDayDatabase::class.java,//и класс бд
            "notes.db"//название бд
        ).build()
    }
    // You need to implement or provide getUserId function
    val userId = "local_user" // Replace with actual user ID or getUserId implementation

    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }

    // Проверка валидности формы
    LaunchedEffect(taskName, dueDate) {
        isFormValid = taskName.isNotBlank() && dueDate.isNotBlank()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок экрана
        Text(
            text = "Новая задача",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Поле названия задачи
        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Название задачи") },
            placeholder = { Text("Введите название") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        // Поле описания
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            label = { Text("Описание") },
            placeholder = { Text("Добавьте детали (необязательно)") },
            maxLines = 5,
            shape = MaterialTheme.shapes.medium
        )

        // Поле даты выполнения
        OutlinedTextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Дата выполнения") },
            placeholder = { Text("ДД.ММ.ГГГГ") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            visualTransformation = DateTransformation(),
            shape = MaterialTheme.shapes.medium
        )

        // Кнопка создания
        Button(
            onClick = {
                val newTask = LocalTask(
                    title = taskName,
                    description = taskDescription,
                    dueDate = dueDate,
                    userId = userId,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis()
                )
                coroutineScope.launch {
                    database.taskDao().upsert(newTask)
                    onTaskCreated() // Уведомляем о создании
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = isFormValid,
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Создать задачу", style = MaterialTheme.typography.labelLarge)
        }
    }
}
