package com.example.yourday

import CustomDatePicker
import YourDayTheme
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Goal
import com.example.yourday.model.Idea
import com.example.yourday.model.Steps
import com.example.yourday.model.Task
import com.example.yourday.model.UserActivity
import com.example.yourday.screens.ArticlesScreen
import com.example.yourday.screens.ProfileScreen
import com.example.yourday.ui.theme.Blue
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Calendar


val mockTasks=mutableListOf<Task>()
val mockGoals=mutableListOf<Goal>()
val mockIdeas=mutableListOf<Idea>()


val mockSteps=mutableListOf<Steps>()
val mockUA=mutableListOf<UserActivity>()




class MainActivity : ComponentActivity() {
    private val authHelper by lazy { SupabaseHelper() }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                                    supabaseHelper = authHelper,
                                    onTaskClick = { taskId ->
                                        val intent = Intent(this@MainActivity, TaskActivity::class.java).apply {
                                            putExtra("taskId", taskId)
                                        }
                                        startActivity(intent)
                                    }
                                )
                            }
                            composable("articles") { ArticlesScreen(
                                navController,
                                onIntentToDetails = {articleId ->
                                    articleId?.let {
                                        startActivity(Intent(this@MainActivity, ArticleDetailActivity::class.java).apply {
                                            putExtra("article_id", articleId)
                                        })
                                    }
                                }) }
                            composable("profile") { ProfileScreen() }
                            composable("daily") {
                                DailyScreen(
                                    supabaseHelper = authHelper,
                                    onTaskClick ={ taskId ->
                                        val intent = Intent(this@MainActivity, TaskActivity::class.java).apply {
                                            putExtra("taskId", taskId)
                                        }
                                        startActivity(intent)
                                    }
                                )
                            }
                            composable("health_and_fitness") { HealthAndFitnessScreen(authHelper) }
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
    supabaseHelper: SupabaseHelper,
    onTaskClick: (Int) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {
            Text(
                text = "Главная",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                    color = Primary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 6.dp),

                )

            // Создаем NavController
            val navController = rememberNavController()

            // Вызываем наш компонент с навигацией
            NavigationWithCustomMenu(
                navController = navController,
                supabaseHelper = supabaseHelper,
                onTaskClick = onTaskClick
            )



        }
    }
}
@Composable
fun NavigationWithCustomMenu(
    navController: NavHostController,
    supabaseHelper: SupabaseHelper,
    onTaskClick: (Int) -> Unit
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
            composable("daily") {
                DailyScreen(
                    supabaseHelper = supabaseHelper,
                    onTaskClick = onTaskClick
                )
            }
            composable("health_and_fitness") { HealthAndFitnessScreen(supabaseHelper) }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(
    supabaseHelper: SupabaseHelper,
    onTaskClick: (Int) -> Unit
) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current
    val userId = getUserId(context)
    val dateStr = convertMillisToDateString(selectedDate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        CustomDatePicker(
            onDateSelected = { millis -> selectedDate = millis },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )

        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TasksSection(
                dateStr,
                userId,
                supabaseHelper,
                onTaskClick = onTaskClick
            )
            GoalsSection(dateStr, userId, supabaseHelper)
            IdeasSection(dateStr, userId, supabaseHelper)
        }
    }
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
    onTaskClick: (Int) -> Unit
) {
    val context = LocalContext.current
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        isLoading = true
        error = null
        try {
            tasks = supabaseHelper.getDailyTasks(date, userId)
        } catch (e: Exception) {
            error = "Failed to load tasks: ${e.message}"
            Log.e("TasksSection", "Error loading tasks", e)
        } finally {
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
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
                    onClick = {
                        val intent = Intent(context, TaskActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить задачу",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }

            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
                tasks.isEmpty() -> Text(
                    text = "Внимание! Обнаружено: 0 задач.\nРекомендуется: чай, плед, любимый сериал.\nСитуация: полный релакс.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        textAlign = TextAlign.Center,
                        color = DarkBlue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top=6.dp)
                )
                else -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tasks.forEach { task ->
                        TaskItem(
                            task = task,
                            onClick = { onTaskClick(task.id) }
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}




@Composable
private fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = task.isCompleted,
            onCheckedChange = { /* Handle completion status change */ }
        ) {
            Image(
                painter = painterResource(
                    if (task.isCompleted) R.drawable.check2
                    else R.drawable.check1
                ),
                contentDescription = if (task.isCompleted) "Checked" else "Unchecked",
                modifier = Modifier.size(24.dp),
                colorFilter = null
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
private fun GoalsSection(date: String, userId: String, supabaseHelper: SupabaseHelper) {
    var goals by remember { mutableStateOf<List<Goal>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        isLoading = true
        error = null
        try {
            goals = supabaseHelper.getDailyGoals(date, userId)
        } catch (e: Exception) {
            error = "Failed to load goals: ${e.message}"
            Log.e("GoalsSection", "Error loading goals", e)
        } finally {
            isLoading = false
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
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
                    onClick = { onIntentToAddScreenTask() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить цель",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }


            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Composable
private fun GoalItem(goalProgress: Goal) {
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
private fun IdeasSection(date: String, userId: String, supabaseHelper: SupabaseHelper) {
    var ideas by remember { mutableStateOf<List<Idea>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        isLoading = true
        error = null
        try {
            ideas = supabaseHelper.getDailyIdeas(date, userId)
        } catch (e: Exception) {
            error = "Failed to load ideas: ${e.message}"
            Log.e("IdeasSection", "Error loading ideas", e)
        } finally {
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
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
                    onClick = { onIntentToAddScreenTask() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить идею",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }

            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Composable
private fun IdeaItem(idea: Idea) {
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
fun HealthAndFitnessScreen(supabaseHelper: SupabaseHelper) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current
    val userId = getUserId(context)
    val dateStr = convertMillisToDateString(selectedDate)


    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()), // Добавляем вертикальный скролл
        verticalArrangement = Arrangement.spacedBy(16.dp) ){// Расстояние 6.dp между элементами)
        CustomDatePicker(
            onDateSelected = { millis ->
                selectedDate = millis
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )
        StepsSection(dateStr, userId, supabaseHelper)
        ActivitySection(dateStr, userId, supabaseHelper)
    }
}
@Composable
private fun StepsSection(date: String, userId: String, supabaseHelper: SupabaseHelper) {
    var steps by remember { mutableStateOf<List<Steps>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        isLoading = true
        error = null
        try {
            steps = supabaseHelper.getStepsData(date, userId)
        } catch (e: Exception) {
            error = "Failed to load steps data: ${e.message}"
            Log.e("StepsSection", "Error loading steps", e)
        } finally {
            isLoading = false
        }
    }
    val totalSteps = remember(steps) {
        steps.sumOf { it.stepsCount }
    }
    val targetSteps = 10000
    val progress = remember(totalSteps) {
        totalSteps.toFloat() / targetSteps
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
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
                else -> {
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
                    }
                }
            }

        }
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

        // Вкладка "Статьи"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable {
                    navController.navigate("articles") {
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
                    id = if (currentRoute == "articles") R.drawable.book_solid
                    else R.drawable.book_outline
                ),
                contentDescription = "Статьи",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Статьи",
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
private fun ActivitySection(date: String, userId: String, supabaseHelper: SupabaseHelper) {
    var activities by remember { mutableStateOf<List<UserActivity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(date) {
        isLoading = true
        error = null
        try {
            activities = supabaseHelper.getUserActivity(date, userId)
        } catch (e: Exception) {
            error = "Failed to load activities: ${e.message}"
            Log.e("ActivitySection", "Error loading activities", e)
        } finally {
            isLoading = false
        }
    }

    val totalMinutes = remember(activities) {
        activities.sumOf { it.durationMinutes }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Purple1),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
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
                    onClick = { onIntentToAddScreenTask() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_ic),
                        contentDescription = "Добавить активность",
                        modifier = Modifier.size(33.dp),
                        tint = Primary
                    )
                }
            }
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
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
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Composable
fun ActivityItem(activity: UserActivity) {
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


private fun onIntentToAddScreenTask(){

}

private fun onIntentToAddScreenNote(){

}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    YourDayTheme {

    }
}