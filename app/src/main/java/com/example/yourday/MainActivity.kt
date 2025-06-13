package com.example.yourday

import CustomDatePicker
import YourDayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.example.yourday.models.BodyMeasurement
import com.example.yourday.models.DailyFinances
import com.example.yourday.models.DailyGoals
import com.example.yourday.models.DailyIdeas
import com.example.yourday.models.DailyNotes
import com.example.yourday.models.DailyScreenData
import com.example.yourday.models.DailyTasks
import com.example.yourday.models.GoalProgress
import com.example.yourday.models.GratitudeJournal
import com.example.yourday.models.HealthData
import com.example.yourday.models.Idea
import com.example.yourday.models.NutritionLog
import com.example.yourday.models.Steps
import com.example.yourday.models.Task
import com.example.yourday.models.UserActivity
import com.example.yourday.models.WaterIntake
import com.example.yourday.ui.theme.Blue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Green
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Red
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Calendar

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
                    Column(modifier = Modifier.fillMaxSize()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        )
                        ) {
                        // Основной контент
                        NavHost(
                            navController = navController,
                            startDestination = "main",
                            modifier = Modifier.weight(1f)
                                .padding(
                                    top = WindowInsets.statusBars.asPaddingValues()
                                        .calculateTopPadding(),
                                    bottom = WindowInsets.navigationBars.asPaddingValues()
                                        .calculateBottomPadding()
                                ),
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                            popEnterTransition = { EnterTransition.None },
                            popExitTransition = { ExitTransition.None }
                        ) {
                            composable("main") {
                                MainScreen(
                                    onIntentToNotification = {},
                                    supabaseHelper = remember { authHelper }
                                )
                            }
                            composable("articles") { ArticlesScreen() }
                            composable("profile") { ProfileScreen() }
                            composable("daily") { DailyScreen(remember { authHelper }, onIntentToChecklist = {

                            },
                                onIntentToQuoteOTheDay = {

                                }) }
                            composable("health_and_fitness") { HealthAndFitnessScreen(remember { authHelper }) }
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
    onIntentToNotification:()-> Unit,
    supabaseHelper: SupabaseHelper // Add this parameter
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Пустой элемент для балансировки (занимает такое же пространство слева, как иконка справа)
                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = "Главная",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        color = Primary
                    ),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = onIntentToNotification) {
                    Icon(
                        painter = painterResource(id = R.drawable.notific),
                        contentDescription = "Уведомления",
                        modifier = Modifier.size(26.dp),
                        tint = Primary
                    )
                }
            }

            // Создаем NavController
            val navController = rememberNavController()

            // Вызываем наш компонент с навигацией
            NavigationWithCustomMenu(
                navController = navController,
                supabaseHelper = supabaseHelper
            )



        }
    }
}
@Composable
fun NavigationWithCustomMenu(navController: NavHostController, supabaseHelper: SupabaseHelper) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(modifier = Modifier.fillMaxSize()) {
        // Верхняя панель с табами
        Column(modifier = Modifier.padding(top = 47.dp)) {
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
            modifier = Modifier.weight(1f),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable("daily") { DailyScreen(supabaseHelper, onIntentToChecklist = {

            },
                onIntentToQuoteOTheDay = {

                }) }
            composable("health_and_fitness") { HealthAndFitnessScreen(supabaseHelper) }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(
    supabaseHelper: SupabaseHelper,
    onIntentToChecklist: () -> Unit,
    onIntentToQuoteOTheDay: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var dailyData by remember { mutableStateOf<DailyScreenData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Convert millis to date string
    fun convertMillisToDateString(millis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        return "$year-$month-$day"
    }

    // Load data when date changes
    LaunchedEffect(selectedDate) {
        isLoading = true
        errorMessage = null
        try {
            val dateStr = convertMillisToDateString(selectedDate)
            dailyData = supabaseHelper.getDailyData(dateStr)
        } catch (e: Exception) {
            errorMessage = "Failed to load data: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomDatePicker(
            onDateSelected = { millis ->
                selectedDate = millis
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )

        Column(
            modifier = Modifier.padding(top=16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
                    .background(Primary, RoundedCornerShape(10.dp)),
                onClick = onIntentToChecklist
            ) {
                Text("Подготовиться к выходу из дома")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
                    .background(Primary, RoundedCornerShape(10.dp)),
                onClick = onIntentToQuoteOTheDay
            ) {
                Text("Ваша цитата дня")
            }


            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage ?: "Unknown error", color = MaterialTheme.colorScheme.error)
                    }
                }
                dailyData == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No data available for selected date")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        dailyData?.let { data ->
                            item { TasksSection(data.tasks) }
                            item { GratitudeJournalSection(data.gratitudeJournal) }
                            item { NotesSection(data.notes) }
                            item { GoalsSection(data.goals) }
                            item { IdeasSection(data.ideas) }
                            item { FinancesSection(data.finances) }
                        }
                    }
                }
            }
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
private fun TasksSection(tasks: DailyTasks) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Задачи",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (tasks.items.isEmpty()) {
                Column {
                    Text(
                        text = "Внимание! Сегодня обнаружено: 0 задач.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Рекомендуется: ${tasks.recommendation}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Чрезвычайная ситуация: ${tasks.emergency}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Сегодня у вас ${tasks.count} задач",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    tasks.items.forEach { task ->
                        TaskItem(task = task)
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskItem(task: Task) {
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            task.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
@Composable
private fun GratitudeJournalSection(journal: GratitudeJournal) {
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Дневник благодарности и радости",
                style = MaterialTheme.typography.titleLarge)

            if (journal.gratitudeItems.isNotEmpty()) {
                Text("Я благодарен за:", style = MaterialTheme.typography.bodyMedium)
                journal.gratitudeItems.forEach { item ->
                    Text("• $item", modifier = Modifier.padding(start = 8.dp))
                }
            }

            if (journal.joyItems.isNotEmpty()) {
                Text("Мои радости:", style = MaterialTheme.typography.bodyMedium)
                journal.joyItems.forEach { item ->
                    Text("• $item", modifier = Modifier.padding(start = 8.dp))
                }
            }

            if (journal.gratitudeItems.isEmpty() && journal.joyItems.isEmpty()) {
                Text("Каждый день – повод для радости! За что вы благодарны сегодня?",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// 1. NotesSection
@Composable
private fun NotesSection(notes: DailyNotes) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Заметки",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (notes.items.isEmpty()) {
                Text(
                    text = "Каждая большая история начинается с первого слова. Что вас вдохновляет сегодня?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                notes.items.forEach { note ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = note.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "\"${note.note}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


// 3. GoalsSection
@Composable
private fun GoalsSection(goals: DailyGoals) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Цели",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (goals.activeGoals.isEmpty()) {
                Text(
                    text = "Какие цели вы хотите достичь? Начните с малого - добавьте свою первую цель!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    goals.activeGoals.forEach { goalProgress ->
                        GoalItem(goalProgress = goalProgress)
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalItem(goalProgress: GoalProgress) {
    Column {
        Text(
            text = goalProgress.goal.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Progress bar
        LinearProgressIndicator(
            progress = goalProgress.progress.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(6.dp),
            color = Blue,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = "${(goalProgress.progress * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )

    }
}

// 4. IdeasSection
@Composable
private fun IdeasSection(ideas: DailyIdeas) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Идеи",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (ideas.items.isEmpty()) {
                Text(
                    text = "Запишите свою первую идею — вдруг это начало чего-то грандиозного?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ideas.items.forEach { idea ->
                        IdeaItem(idea = idea)
                    }
                }
            }
        }
    }
}

@Composable
private fun IdeaItem(idea: Idea) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "–", modifier = Modifier.padding(end = 8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = idea.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            idea.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

// 5. FinancesSection
@Composable
private fun FinancesSection(finances: DailyFinances) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Доходы и расходы",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (finances.lastTransaction == null) {
                Text(
                    text = "Ваша первая запись появится здесь. Начните с малого — зафиксируйте сегодняшний кофе или проезд.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Баланс
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Баланс:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${finances.balance} ₽",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (finances.balance >= 0) Green else Red
                        )
                    }

                    // Доходы/расходы
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Доходы: ${finances.income} ₽",
                            style = MaterialTheme.typography.bodySmall,
                            color = Green
                        )
                        Text(
                            text = "Расходы: ${finances.expenses} ₽",
                            style = MaterialTheme.typography.bodySmall,
                            color = Red
                        )
                    }

                    // Последняя операция
                    Text(
                        text = "Последняя операция:",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    finances.lastTransaction?.let { transaction ->
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = transaction.date)
                                Text(
                                    text = "${transaction.amount} ₽",
                                    color = if (transaction.amount >= 0) Green else Red
                                )
                            }
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            transaction.description?.let { description ->
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAndFitnessScreen(supabaseHelper: SupabaseHelper) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var healthData by remember { mutableStateOf<HealthData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedDate) {
        isLoading = true
        errorMessage = null
        try {
            val dateStr = convertMillisToDateString(selectedDate)
            healthData = supabaseHelper.getHealthDataForDate(dateStr)
        } catch (e: Exception) {
            errorMessage = "Failed to load health data: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomDatePicker(
            onDateSelected = { millis ->
                selectedDate = millis
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp)
        )

        when {
            isLoading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).padding(top = 20.dp))
            errorMessage != null -> Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            healthData == null -> Text("No health data available")
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { StepsSection(healthData!!.steps) }
                    item { ActivitySection(healthData!!.activity) }
                    item { NutritionSection(healthData!!.nutrition) }
                    item { WaterSection(healthData!!.water) }
                    item { MeasurementsSection(healthData!!.measurements) }
                }
            }
        }
    }
}
@Composable
private fun StepsSection(steps: List<Steps>) {
    val stepData = steps.firstOrNull() ?: return
    val distanceKm=0.0
    val caloriesBurned=0.0
    val durationMinutes=0.0

    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Шаги", style = MaterialTheme.typography.titleLarge)
            LinearProgressIndicator(
                progress = (stepData.stepsCount.toFloat() / 10000),
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
            Text("${stepData.stepsCount}/10 000 шагов (${(stepData.stepsCount.toFloat() / 10000 * 100).toInt()}%)")
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${distanceKm} км")
                Text("${caloriesBurned} ккал")
                Text("${durationMinutes} мин")
            }
        }
    }
}



@Composable
private fun WaterSection(water: List<WaterIntake>) {
    val waterData = water.firstOrNull() ?: return
    val targetMl=2500.0
    Card(modifier = Modifier.padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Вода", style = MaterialTheme.typography.titleLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    progress = (waterData.amountMl / targetMl).toFloat(),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("${waterData.amountMl/1000}/${targetMl/1000} л")
            }
            Text("Регулярное употребление воды снижает риск инфаркта.",
                style = MaterialTheme.typography.bodySmall)
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
                .clickable { navController.navigate("main"){
                    anim {
                        enter = 0
                        exit = 0
                        popEnter = 0
                        popExit = 0
                    }
                    launchSingleTop = true
                } }
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
                .clickable { navController.navigate("articles"){
                    anim {
                        enter = 0
                        exit = 0
                        popEnter = 0
                        popExit = 0
                    }
                    launchSingleTop = true
                } }
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
                .clickable { navController.navigate("profile"){
                    anim {
                        enter = 0
                        exit = 0
                        popEnter = 0
                        popExit = 0
                    }
                    launchSingleTop = true
                } }
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
private fun ActivitySection(activities: List<UserActivity>) {
    val lastActivity = activities.firstOrNull()
    val activityTypes = remember { listOf("Прогулка", "Пробежка", "Тренировка") } // Example types

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Активность",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (activities.isEmpty()) {
                Column {
                    Text(
                        text = "Добавьте прогулку, пробежку или тренировку — маленькие шаги приводят к большим результатам!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Summary stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "400.3 ккал",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "1 ч. 00 мин.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Last activity
                    Text(
                        text = "Последняя активность",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    lastActivity?.let { activity ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "250 ккал · 30 мин. · Бег",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Сегодня, 08:30",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NutritionSection(nutritionLogs: List<NutritionLog>) {
    val lastMeal = nutritionLogs.firstOrNull()

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Прием пищи",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (nutritionLogs.isEmpty()) {
                Column {
                    Text(
                        text = "Начните с первого приема пищи — даже маленькая запись помогает контролировать рацион.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Summary stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "300.0 ккал", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "8.0 г", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "400.3 г", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "400.3 г", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Last meal
                    Text(
                        text = "Последний прием пищи",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    lastMeal?.let { meal ->
                        Column {
                            Text(
                                text = "Овсянка с фруктами",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "300 ккал · 8.0 г · 5.0 г · 55 г · Завтрак",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Сегодня, 08:30",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MeasurementsSection(measurements: List<BodyMeasurement>) {
    val latestMeasurement = measurements.firstOrNull()

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Измерения тела",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (measurements.isEmpty()) {
                Column {
                    Text(
                        text = "Добавьте первое измерение — это поможет отслеживать ваш прогресс!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    latestMeasurement?.let { measurement ->
                        // Weight with change
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Вес:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "79 кг (-3.0 кг)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Chest measurement
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Грудь:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "105 см (0 см)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Waist measurement
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Талия:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "79 см (-1 см)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Hips measurement
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Бедра:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "111 см (-2 см)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    YourDayTheme {

    }
}