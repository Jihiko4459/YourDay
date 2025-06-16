package com.example.yourday

import CustomDatePicker
import YourDayTheme
import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.database.InitialDataWorker
import com.example.yourday.database.YourDayDatabase
import com.example.yourday.factory.daily.IdeaViewModelFactory
import com.example.yourday.factory.goals_and_habits.GoalViewModelFactory
import com.example.yourday.factory.health_and_fitness.StepsViewModelFactory
import com.example.yourday.factory.health_and_fitness.UserActivityViewModelFactory
import com.example.yourday.factory.tasks.TaskViewModelFactory
import com.example.yourday.model.LocalGoal
import com.example.yourday.model.LocalIdea
import com.example.yourday.model.LocalSteps
import com.example.yourday.model.LocalTask
import com.example.yourday.model.LocalUserActivity
import com.example.yourday.repository.daily.IdeaRepository
import com.example.yourday.repository.goals_and_habits.GoalRepository
import com.example.yourday.repository.health_and_fitness.StepsRepository
import com.example.yourday.repository.health_and_fitness.UserActivityRepository
import com.example.yourday.repository.tasks.TaskRepository
import com.example.yourday.screens.ProfileScreen
import com.example.yourday.ui.theme.Blue
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1
import com.example.yourday.ui.theme.White
import com.example.yourday.viewmodel.daily.IdeaViewModel
import com.example.yourday.viewmodel.goals_and_habits.GoalViewModel
import com.example.yourday.viewmodel.health_and_fitness.StepsViewModel
import com.example.yourday.viewmodel.health_and_fitness.UserActivityViewModel
import com.example.yourday.viewmodel.tasks.TaskViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar


class MainActivity : ComponentActivity() {
    private val REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 1001


    private val authHelper by lazy { SupabaseHelper() }
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,//передаем контекст приложения
            YourDayDatabase::class.java,//и класс бд
            "notes.db"//название бд
        ).build()
    }//создаем объект бд



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            YourDayTheme {

                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                if (!prefs.getBoolean("data_loaded", false)) {
                    InitialDataWorker.enqueue(this)
                    prefs.edit().putBoolean("data_loaded", true).apply()
                }

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

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                        ) {
                        // Основной контент
                        NavHost(
                            navController = navController,
                            startDestination = "main",
                            modifier = Modifier.weight(1f)
                        ) {
                            composable("main") {
                                MainScreen(
                                    database = database,
                                    supabaseHelper = authHelper
                                )
                            }
                            composable("profile") { ProfileScreen(database) }
                            composable("daily") { DailyScreen(database, authHelper) }
                            composable("health_and_fitness") { HealthAndFitnessScreen(database, authHelper) }

                        }

                        // Нижняя панель навигации
                        BottomNavigationBar(navController)
                    }
                }
            }
        }
    }
    // Обработка кнопки "Назад"
    override fun onBackPressed() {
        super.onBackPressed()
        // Дополнительные действия при необходимости
        finish() // Закрывает текущую Activity
    }
}

@Composable
fun MainScreen(
    database: YourDayDatabase,
    supabaseHelper: SupabaseHelper
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Главная",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                    color = Primary
                ),
                textAlign = TextAlign.Center
            )

            // Создаем NavController
            val navController = rememberNavController()

            // Вызываем наш компонент с навигацией
            NavigationWithCustomMenu(
                navController = navController,
                database = database,
                supabaseHelper = supabaseHelper
            )
        }
    }
}
@Composable
fun NavigationWithCustomMenu(
    navController: NavHostController,
    database: YourDayDatabase,
    supabaseHelper: SupabaseHelper
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(modifier = Modifier.fillMaxSize()) {
        // Верхняя панель с табами
        Column(modifier = Modifier.padding(top = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Вкладка "Ежедневник"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("daily") {
                                anim { enter = 0; exit = 0; popEnter = 0; popExit = 0 }
                                launchSingleTop = true
                            }
                        }
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        "Ежедневник",
                        color = if (currentRoute == "daily") Primary else Gray1,
                        style = TextStyle(
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        ),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    if (currentRoute == "daily") {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .wrapContentWidth()
                                .background(Primary, RoundedCornerShape(10.dp)))
                    }
                }

                // Вкладка "Здоровье и фитнес"
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("health_and_fitness") {
                                anim { enter = 0; exit = 0; popEnter = 0; popExit = 0 }
                                launchSingleTop = true
                            }
                        }
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        "Здоровье и фитнес",
                        color = if (currentRoute == "health_and_fitness") Primary else Gray1,
                        style = TextStyle(
                            fontSize = 19.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        ),
                        modifier = Modifier.padding(vertical = 12.dp),
                        softWrap = false
                    )

                    if (currentRoute == "health_and_fitness") {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .wrapContentWidth()
                                .background(Primary, RoundedCornerShape(10.dp)))
                    }
                }
            }
        }

        // Основной контент
        NavHost(
            navController = navController,
            startDestination = "daily",
            modifier = Modifier.weight(1f)
        ) {
            composable("daily") { DailyScreen(database, supabaseHelper) }
            composable("health_and_fitness") { HealthAndFitnessScreen(database, supabaseHelper) }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(
    database: YourDayDatabase,
    supabaseHelper: SupabaseHelper
) {
    val context = LocalContext.current
    val app = context.applicationContext as Application

    // Инициализация ViewModel для каждой секции
    val viewModelTask: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(app, TaskRepository(database)))
    val viewModelGoal: GoalViewModel = viewModel(
        factory = GoalViewModelFactory(app, GoalRepository(database)))
    val viewModelIdea: IdeaViewModel = viewModel(
            factory = IdeaViewModelFactory(app, IdeaRepository(database)))

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val userId = getUserId(context)
    val dateStr = convertMillisToDateString(selectedDate)

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Добавляем вертикальный скролл
        verticalArrangement = Arrangement.spacedBy(6.dp) ){// Расстояние 6.dp между элементами)
        CustomDatePicker(
            onDateSelected = { millis ->
                selectedDate = millis
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )

        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Загружаем данные при изменении даты
            LaunchedEffect(dateStr) {
                viewModelTask.loadTasksByDate(dateStr)
                viewModelGoal.loadGoals()
                viewModelIdea.loadIdeasByDate(dateStr)
            }
            // Секции с использованием ViewModel
            TasksSection(
                date = dateStr,
                userId = userId,
                supabaseHelper = supabaseHelper,
                tasksState = viewModelTask.tasksByDate
            )

            GoalsSection(
                date = dateStr,
                userId = userId,
                supabaseHelper = supabaseHelper,
                goalsState = viewModelGoal.goals
            )

            IdeasSection(
                date = dateStr,
                userId = userId,
                supabaseHelper = supabaseHelper,
                ideasState = viewModelIdea.ideas
            )
        }
    }
}



private  fun onIntentToAddTask(context: Context){
    val intent = Intent(context, AddTaskActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}



private  fun onIntentToAddGoal(context: Context){
    val intent = Intent(context, AddGoalActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

private  fun onIntentToAddIdea(context: Context){
    val intent = Intent(context, AddIdeaActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}

private  fun onIntentToAddUA(context: Context){
    val intent = Intent(context, AddUAActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}



private fun getUserId(context: Context): String {
    val sharedPref = context.getSharedPreferences("authorization", MODE_PRIVATE)
    return sharedPref.getString("USER_ID", "") ?: ""
}


// Вспомогательная функция
private fun convertMillisToDateString(millis: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
    val year = calendar.get(Calendar.YEAR)
    val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
    val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
    return "$year-$month-$day"
}

@Composable
private fun TasksSection(
    date: String,
    userId: String,
    supabaseHelper: SupabaseHelper,
    tasksState: StateFlow<List<LocalTask>>
) {
    val tasks by tasksState.collectAsState()
    val context=LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(top=6.dp, bottom = 16.dp, start = 16.dp, end=16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                    Icon(
                        painter = painterResource(id = R.drawable.task_ic),
                        contentDescription = "Задачи",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                    Text(
                        text = "Задачи",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold))
                        ),
                        color = DarkBlue,
                        modifier = Modifier.padding(3.dp)
                    )
                }

                IconButton(
                    onClick = { onIntentToAddTask(context) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить задачу",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }

            when {
                tasks.isEmpty() ->Text(
                    text = "Внимание! Сегодня обнаружено: 0 задач.\nРекомендуется: чай, плед, любимый сериал.\nЧрезвычайная ситуация: полный релакс.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center,
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top=6.dp)
                )
                else -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tasks.forEach { task -> TaskItem(task) }
                }
            }
        }
    }
}



@Composable
private fun TaskItem(task: LocalTask) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = task.isCompleted,
            onCheckedChange = { /* Обработка изменения статуса */ }
        ) {
            Image(
                painter = painterResource(
                    if (task.isCompleted) R.drawable.check2
                    else R.drawable.check1
                ),
                contentDescription = if (task.isCompleted) "Checked" else "Unchecked",
                modifier = Modifier.size(24.dp),
                colorFilter = null // Отключаем стандартный tint
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    textAlign = TextAlign.Center,
                    color = DarkBlue
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// 3. GoalsSection
@Composable
private fun GoalsSection(
    date: String,
    userId: String,
    supabaseHelper: SupabaseHelper,
    goalsState: StateFlow<List<LocalGoal>>
) {
    val goals by goalsState.collectAsState()
    val context=LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(top=6.dp, bottom = 16.dp, start = 16.dp, end=16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                    Icon(
                        painter = painterResource(id = R.drawable.goal_ic),
                        contentDescription = "Цели",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                    Text(
                        text = "Цели",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold))
                        ),
                        color = DarkBlue,
                        modifier = Modifier.padding(3.dp)
                    )
                }

                IconButton(
                    onClick = { onIntentToAddGoal(context) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить цель",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }


            when {
                goals.isEmpty() -> Text(
                        text="Какие цели вы хотите достичь? Начните с малого - добавьте свою первую цель!",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            textAlign = TextAlign.Center,
                            color = DarkBlue
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top=6.dp)
                    )
                else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    goals.forEach { goal -> GoalItem(goal) }
                }
            }
        }
    }
}

@Composable
private fun GoalItem(goalProgress: LocalGoal) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
            Icon(
                painter = painterResource(id = R.drawable.notific),
                contentDescription = "Уведомления",
                modifier = Modifier.size(24.dp),
                tint = Primary
            )

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = goalProgress.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Progress bar
        LinearProgressIndicator(
            progress = goalProgress.progressGoal.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(6.dp),
            color = Blue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = "${(goalProgress.progressGoal * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )

    }
}

// 4. IdeasSection
@Composable
private fun IdeasSection(
    date: String,
    userId: String,
    supabaseHelper: SupabaseHelper,
    ideasState: StateFlow<List<LocalIdea>>
) {
    val ideas by ideasState.collectAsState()
    val context=LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(top=6.dp, bottom = 16.dp, start = 16.dp, end=16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                    Icon(
                        painter = painterResource(id = R.drawable.light_bulb_on_ic),
                        contentDescription = "Идея",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                    Text(
                        text = "Идеи",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold))
                        ),
                        color = DarkBlue,
                        modifier = Modifier.padding(3.dp)
                    )
                }

                IconButton(
                    onClick = { onIntentToAddIdea(context) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить идею",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }

            when {
                ideas.isEmpty() -> Text(
                    text="Запишите свою первую идею — вдруг это начало чего-то грандиозного?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center,
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top=6.dp)
                )
                else -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ideas.forEach { idea -> IdeaItem(idea) }
                }
            }
        }
    }
}

@Composable
private fun IdeaItem(idea: LocalIdea) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "–", modifier = Modifier.padding(start = 3.dp, end = 8.dp))
        Text(
            text = idea.title,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                textAlign = TextAlign.Center,
                color = DarkBlue
            )
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAndFitnessScreen(
    database: YourDayDatabase,
    supabaseHelper: SupabaseHelper
) {
    val context = LocalContext.current
    val app = context.applicationContext as Application

    val viewModelSteps: StepsViewModel = viewModel(
        factory = StepsViewModelFactory(app, StepsRepository(database))
    )

    // Состояние для отображения объяснения необходимости разрешения
    var showPermissionRationale by remember { mutableStateOf(false) }

    // Лончер для запроса разрешения
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModelSteps.startStepTracking()
        } else {
            showPermissionRationale = true
        }
    }

    // Проверка и запрос разрешения при первом открытии экрана
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModelSteps.startStepTracking()
        } else {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    // Диалог объяснения необходимости разрешения
    if (showPermissionRationale) {
        AlertDialog(
            onDismissRequest = { showPermissionRationale = false },
            title = { Text("Необходимо разрешение") },
            text = { Text("Для подсчета шагов необходимо разрешение на распознавание активности") },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionRationale = false
                        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showPermissionRationale = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    val viewModelActivity: UserActivityViewModel = viewModel(
        factory = UserActivityViewModelFactory(app, UserActivityRepository(database))
    )

    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val userId = getUserId(context)
    val dateStr = convertMillisToDateString(selectedDate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomDatePicker(
            onDateSelected = { millis ->
                selectedDate = millis
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )

        LaunchedEffect(dateStr) {
            viewModelSteps.loadStepsByDate(dateStr)
            viewModelActivity.loadActivitiesByDate(dateStr)
        }

        StepsSection(
            date = dateStr,
            userId = userId,
            supabaseHelper = supabaseHelper,
            stepsState = viewModelSteps.steps
        )

        ActivitySection(
            date = dateStr,
            userId = userId,
            supabaseHelper = supabaseHelper,
            activitiesState = viewModelActivity.activities
        )
    }
}


@Composable
private fun StepsSection(
    date: String,
    userId: String,
    supabaseHelper: SupabaseHelper,
    stepsState: StateFlow<List<LocalSteps>>,
    viewModel: StepsViewModel = viewModel() // Добавляем viewModel как параметр
) {


    val steps by stepsState.collectAsState()
    val currentSteps = steps.firstOrNull() ?: LocalSteps(userId = userId, date = date, stepsCount = 0)
    val totalSteps = currentSteps.stepsCount
    val targetSteps = 10000
    val progress = remember(totalSteps) { (totalSteps.toFloat() / targetSteps).coerceIn(0f, 1f) }

    // Управление жизненным циклом сенсора
    LaunchedEffect(Unit) {
        viewModel.startStepTracking()
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(top=6.dp, start = 6.dp, end = 6.dp, bottom = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                    Icon(
                        painter = painterResource(id = R.drawable.foot_ic),
                        contentDescription = "Шаги",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                    Text(
                        text = "Шаги",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold))
                        ),
                        color = DarkBlue,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$totalSteps ",
                    style = TextStyle(
                        fontSize = 22.sp,  // Больший размер для текущих шагов
                        fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    ),
                    color = DarkBlue
                )
                Text(
                    text = "/ $targetSteps шагов",
                    style = TextStyle(
                        fontSize = 14.sp,  // Обычный размер для целевых шагов
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = DarkBlue.copy(alpha = 0.8f)
                    )
                )
            }        }

        }
    }





@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Вкладка "Главная"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    navController.navigate("main") {
                        anim {
                            enter = 0
                            exit = 0
                            popEnter = 0
                            popExit = 0
                        }
                        launchSingleTop = true
                    }
                }
                .padding(8.dp)
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(
                    id = if (currentRoute == "main") R.drawable.home_solid
                    else R.drawable.home_outline
                ),
                contentDescription = "Главная",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Главная",
                color = White,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold))
                )
            )
        }

        // Вкладка "Профиль"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    navController.navigate("profile") {
                        anim {
                            enter = 0
                            exit = 0
                            popEnter = 0
                            popExit = 0
                        }
                        launchSingleTop = true
                    }
                }
                .padding(8.dp)
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(
                    id = if (currentRoute == "profile") R.drawable.person_solid
                    else R.drawable.person_outline
                ),
                contentDescription = "Профиль",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Профиль",
                color = White,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold))
                )
            )
        }
    }
}

@Composable
private fun ActivitySection(
    date: String,
    userId: String,
    supabaseHelper: SupabaseHelper,
    activitiesState: StateFlow<List<LocalUserActivity>>
) {
    val activities by activitiesState.collectAsState()
    val totalMinutes = remember(activities) { activities.sumOf { it.durationMinutes } }


    val context=LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(top=6.dp, bottom = 16.dp, start = 16.dp, end=16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                    Icon(
                        painter = painterResource(id = R.drawable.cyclist_ic),
                        contentDescription = "Активность",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                    Text(
                        text = "Активность",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold))
                        ),
                        color = DarkBlue,
                        modifier = Modifier.padding(3.dp)
                    )
                }

                IconButton(
                    onClick = { onIntentToAddUA(context) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить активность",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }
            when {
                activities.isEmpty() -> Text(
                    text="Добавьте прогулку, пробежку или тренировку — маленькие шаги приводят к большим результатам!",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center,
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top=6.dp)
                )
                else -> {
                    Text("Всего активности: ${totalMinutes} минут")
                    activities.forEach { activity -> ActivityItem(activity) }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: LocalUserActivity) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            "Активность: ${activity.activityTypeId ?: "Не указано"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            "Продолжительность: ${activity.durationMinutes} мин",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    YourDayTheme {

    }
}