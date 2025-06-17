package com.example.yourday

import YourDayTheme
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.alpha
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

// Mock data for when Supabase fails
val mockTaskTypes = listOf(
    TaskType(1, "Работа", "work_ic"),
    TaskType(2, "Учёба", "study_ic"),
    TaskType(3, "Здоровье", "health_ic"),
    TaskType(4, "Семья", "family_ic"),
    TaskType(5, "Друзья", "friends_ic"),
    TaskType(6, "Хобби", "hobby_ic"),
    TaskType(7, "Финансы", "finance_ic"),
    TaskType(8, "Личное", "personal_ic")
)

val mockPriorities = listOf(
    TaskPriorityType(1, "Низкий (Не срочно и не критично)"),
    TaskPriorityType(2, "Средний (Важно, но не срочно)"),
    TaskPriorityType(3, "Высокий (Срочно и важно)")
)

class TaskActivity : ComponentActivity() {
    private val supabaseHelper by lazy { SupabaseHelper() }

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
                            taskId = null,
                            supabaseHelper = supabaseHelper,
                            onBack = { finish() }
                        )
                    }
                    composable("taskDetail/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
                        TaskDetailScreen(
                            taskId = taskId,
                            supabaseHelper = supabaseHelper,
                            onBack = { finish() }
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
    onBack: () -> Unit
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
    var dueDate by remember { mutableStateOf("") }

    // Dropdown states
    var taskTypeDropdownExpanded by remember { mutableStateOf(false) }
    var priorityDropdownExpanded by remember { mutableStateOf(false) }
    var taskTypes by remember { mutableStateOf<List<TaskType>>(emptyList()) }
    var priorities by remember { mutableStateOf<List<TaskPriorityType>>(emptyList()) }

    // Load task types and priorities
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            taskTypes = supabaseHelper.getTaskTypes()
            priorities = supabaseHelper.getTaskPriorities()
        } catch (e: Exception) {
            error = "Failed to load task types: ${e.message}"
            taskTypes = mockTaskTypes
            priorities = mockPriorities
        } finally {
            isLoading = false
        }
    }

    // Load task if editing existing one
    LaunchedEffect(taskId) {
        if (taskId != null && !isEditing) {
            isLoading = true
            try {
                task = supabaseHelper.getTaskById(taskId, userId)
                title = task?.title ?: ""
                description = task?.description ?: ""
                task?.taskTypeId?.let { typeId ->
                    selectedTaskType = taskTypes.find { it.id == typeId }
                }
                task?.priorityId?.let { priorityId ->
                    selectedPriority = priorities.find { it.id == priorityId }
                }
                dueDate = task?.dueDate ?: ""
            } catch (e: Exception) {
                error = "Failed to load task: ${e.message}"
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
                modifier = Modifier.fillMaxWidth(),
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

                    if (isEditing) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedTaskType?.typeName ?: "",
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                },
                                placeholder = { Text("Выберите тип задачи") },
                                shape = RoundedCornerShape(12.dp),
                                colors = outlinedTextFieldColors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            DropdownMenu(
                                expanded = taskTypeDropdownExpanded,
                                onDismissRequest = { taskTypeDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                taskTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.typeName) },
                                        onClick = {
                                            selectedTaskType = type
                                            taskTypeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .alpha(0f)
                                .clickable { taskTypeDropdownExpanded = true }
                        )
                    } else {
                        Text(
                            text = selectedTaskType?.typeName ?: "Не указано",
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

                    if (isEditing) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedPriority?.typeName ?: "",
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                },
                                placeholder = { Text("Выберите приоритет") },
                                shape = RoundedCornerShape(12.dp),
                                colors = outlinedTextFieldColors(
                                    focusedBorderColor = Primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            DropdownMenu(
                                expanded = priorityDropdownExpanded,
                                onDismissRequest = { priorityDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                priorities.forEach { priority ->
                                    DropdownMenuItem(
                                        text = { Text(priority.typeName) },
                                        onClick = {
                                            selectedPriority = priority
                                            priorityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .alpha(0f)
                                .clickable { priorityDropdownExpanded = true }
                        )
                    } else {
                        Text(
                            text = selectedPriority?.typeName ?: "Не указано",
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
                        OutlinedTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("ДД.ММ.ГГГГ") },
                            visualTransformation = DateTransformation(),
                            shape = RoundedCornerShape(12.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    } else {
                        Text(
                            text = dueDate,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

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
                                        try {
                                            supabaseHelper.deleteTask(taskId)
                                            onBack()
                                        } catch (e: Exception) {
                                            error = "Failed to delete task: ${e.message}"
                                        }
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
                                val currentTask = Task(
                                    id = taskId ?: 0,
                                    userId = userId,
                                    title = title,
                                    description = description,
                                    taskTypeId = selectedTaskType?.id,
                                    priorityId = selectedPriority?.id,
                                    createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                    dueDate = dueDate,
                                    isCompleted = task?.isCompleted ?: false,
                                    completedAt = ""
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        supabaseHelper.saveTask(currentTask)
                                        onBack()
                                    } catch (e: Exception) {
                                        error = "Failed to save task: ${e.message}"
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