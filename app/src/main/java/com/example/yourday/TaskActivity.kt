package com.example.yourday

import YourDayTheme
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.data.TaskRepository
import com.example.yourday.model.Task
import com.example.yourday.model.TaskPriorityType
import com.example.yourday.model.TaskType
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Red
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TaskActivity : ComponentActivity() {
    private val supabaseHelper by lazy { SupabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YourDayTheme {
                val systemUiController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = darkTheme
                    )

                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = !darkTheme,
                        navigationBarContrastEnforced = true
                    )
                }

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "taskDetail"
                ) {
                    composable("taskDetail") {
                        TaskDetailScreen(
                            taskId = intent.getIntExtra("taskId", 0).takeIf { it != 0 },
                            supabaseHelper = supabaseHelper,
                            onBack = { finish() },
                            onTaskSaved = {
                                // Устанавливаем результат, который будет обработан в MainActivity
                                setResult(RESULT_OK)
                                finish()
                            }
                        )
                    }
                    composable("taskDetail/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                        TaskDetailScreen(
                            taskId = taskId,
                            supabaseHelper = supabaseHelper,
                            onBack = { finish() },
                            onTaskSaved = {
                                setResult(RESULT_OK)
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getUserId(context: Context): String {
    val sharedPref = context.getSharedPreferences("authorization", MODE_PRIVATE)
    return sharedPref.getString("USER_ID", "") ?: ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int?,
    supabaseHelper: SupabaseHelper,
    onBack: () -> Unit,
    onTaskSaved: () -> Unit // Новый параметр для callback
) {
    val context = LocalContext.current
    val userId = getUserId(context)

    var task by remember { mutableStateOf<Task?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(taskId == null) }

    // Task fields
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTaskType by remember { mutableStateOf<TaskType?>(null) }
    var selectedPriority by remember { mutableStateOf<TaskPriorityType?>(null) }

    // Для внутреннего хранения даты в формате yyyy-MM-dd
    var internalDueDate by remember {
        mutableStateOf(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )
    }

    // Для отображения даты в формате dd.MM.yyyy
    var displayDueDate by remember(internalDueDate) {
        mutableStateOf(
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(internalDueDate)
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            } catch (e: Exception) {
                internalDueDate
            }
        )
    }

    val taskTypes = remember { TaskRepository.mockTaskTypes }
    val priorities = remember { TaskRepository.mockPriorities }

    LaunchedEffect(taskId) {
        isLoading = true

        if (taskId == null) {
            selectedTaskType = taskTypes.firstOrNull()
            selectedPriority = priorities.firstOrNull()
            isLoading = false
        } else {
            try {
                task = TaskRepository.getTaskById(taskId, userId, supabaseHelper)
                task?.let {
                    title = it.title ?: ""
                    description = it.description ?: ""
                    internalDueDate = it.dueDate ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    // Устанавливаем выбранные тип и приоритет
                    selectedTaskType = taskTypes.find { type -> type.id == it.taskTypeId }
                    selectedPriority = priorities.find { priority -> priority.id == it.priorityId }
                }
            } catch (e: Exception) {
                error = "Ошибка при загрузке данных: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
                .padding(horizontal = 22.dp)
        ) {
            // Header similar to MainScreen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Primary
                    )
                }

                Text(
                    text = if (isEditing) "Новая задача" else "Задача",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        color = Primary
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                if (!isEditing) {
                    IconButton(
                        onClick = { isEditing = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.pencil_ic),
                            contentDescription = "Редактировать",
                            tint = Primary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Task Name
                    Text(
                        text = "Наименование задачи",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (isEditing) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Введите название задачи") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    } else {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (isEditing) {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            placeholder = { Text("Введите описание задачи") },
                            singleLine = false,
                            shape = RoundedCornerShape(12.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    } else {
                        Text(
                            text = description ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Task Type
                    Text(
                        text = "Тип задачи",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // For Task Type dropdown
                    if (isEditing) {
                        var expanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                readOnly = true,
                                value = selectedTaskType?.typeName ?: "",
                                onValueChange = {},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                placeholder = { Text("Выберите тип задачи") },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.exposedDropdownSize()
                            ) {
                                taskTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.typeName) },
                                        onClick = {
                                            selectedTaskType = type
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = selectedTaskType?.typeName ?: "Не выбрано",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Priority
                    Text(
                        text = "Приоритет задачи",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Priority dropdown - исправленная версия
                    if (isEditing) {
                        var priorityExpanded by remember { mutableStateOf(false) }

                        // Проверка на пустой список приоритетов
                        if (priorities.isEmpty()) {
                            Text("Нет доступных приоритетов", color = MaterialTheme.colorScheme.error)
                        } else {
                            ExposedDropdownMenuBox(
                                expanded = priorityExpanded,
                                onExpandedChange = { priorityExpanded = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    readOnly = true,
                                    value = selectedPriority?.typeName ?: "",
                                    onValueChange = {},
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded)
                                    },
                                    placeholder = { Text("Выберите приоритет") },
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = priorityExpanded,
                                    onDismissRequest = { priorityExpanded = false },
                                    modifier = Modifier.exposedDropdownSize()
                                ) {
                                    priorities.forEach { priority ->
                                        DropdownMenuItem(
                                            text = { Text(priority.typeName) },
                                            onClick = {
                                                selectedPriority = priority
                                                priorityExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = selectedPriority?.typeName ?: "Не выбрано",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Due Date
                    Text(
                        text = "Дата выполнения",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (isEditing) {
                        // Добавляем валидацию даты
                        val isDateValid = remember(displayDueDate) {
                            try {
                                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(displayDueDate)
                                true
                            } catch (e: Exception) {
                                false
                            }
                        }

                        OutlinedTextField(
                            value = displayDueDate,
                            onValueChange = { newValue ->
                                // Ограничиваем длину и разрешаем только цифры и точки
                                if (newValue.length <= 10 && newValue.matches(Regex("[0-9.]*"))) {
                                    displayDueDate = newValue

                                    // Пытаемся преобразовать введенную дату в формат для Supabase
                                    try {
                                        val parsedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(newValue)
                                        if (parsedDate != null) {
                                            internalDueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                                        }
                                    } catch (e: Exception) {
                                        // Ошибка преобразования - оставляем предыдущее значение
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("дд.мм.гггг") },
                            isError = !isDateValid,
                            supportingText = {
                                if (!isDateValid) {
                                    Text("Используйте формат дд.мм.гггг")
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                errorBorderColor = Red
                            )
                        )
                    } else {
                        Text(
                            text = if (internalDueDate.isNotEmpty()) {
                                try {
                                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(internalDueDate)
                                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
                                } catch (e: Exception) {
                                    internalDueDate
                                }
                            } else "Не указано",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(32.dp))

                    // Bottom buttons
                    // Bottom buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (taskId != null) {
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        TaskRepository.deleteTask(taskId!!, userId, supabaseHelper)
                                        onBack()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Red
                                ),
                                border = BorderStroke(1.dp, Red)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                Spacer(Modifier.width(8.dp))
                                Text("Удалить")
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Button(
                            onClick = {
                                // Объявляем переменную для даты выполнения
                                val finalDueDate = try {
                                    val parsedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(displayDueDate)
                                    // Если дата валидна, сохраняем в internalDueDate в формате yyyy-MM-dd
                                    if (parsedDate != null) {
                                        internalDueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                                    }
                                    internalDueDate
                                } catch (e: Exception) {
                                    // Если дата невалидна, используем текущую дату
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                }

                                val currentTask = Task(
                                    id = taskId ?: 0,
                                    userId = userId,
                                    title = title,
                                    description = description,
                                    taskTypeId = selectedTaskType?.id,
                                    priorityId = selectedPriority?.id,
                                    createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                    dueDate = try {
                                        val parsedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(displayDueDate)
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                                    } catch (e: Exception) {
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                    },
                                    isCompleted = task?.isCompleted ?: false,
                                    completedAt = task?.completedAt ?: ""
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        TaskRepository.saveTask(currentTask, supabaseHelper)
                                        onTaskSaved()
                                    } catch (e: Exception) {
                                        // Обработка ошибки сохранения
                                        error = "Ошибка при сохранении задачи: ${e.message}"
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = White
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Сохранить")
                            Spacer(Modifier.width(8.dp))
                            Text("Сохранить")
                        }
                    }
                }
            }
        }
    }
}