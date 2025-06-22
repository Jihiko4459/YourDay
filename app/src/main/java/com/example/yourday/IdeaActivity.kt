package com.example.yourday

import YourDayTheme
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.yourday.api.SupabaseHelper
import com.example.yourday.model.Idea
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Red
import com.example.yourday.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IdeaActivity : ComponentActivity() {
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
                }

                IdeaDetailScreen(
                    ideaId = intent.getIntExtra("ideaId", 0).takeIf { it != 0 },
                    supabaseHelper = supabaseHelper,
                    onBack = { finish() },
                    onIdeaSaved = {
                        setResult(RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailScreen(
    ideaId: Int?,
    supabaseHelper: SupabaseHelper,
    onBack: () -> Unit,
    onIdeaSaved: () -> Unit
) {
    val context = LocalContext.current
    val userId = getUserId(context)

    var idea by remember { mutableStateOf<Idea?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(ideaId == null) }

    // Fields for editing
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // For internal storage (yyyy-MM-dd)
    var internalDate by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    // For display (dd.MM.yyyy)
    var displayDate by remember(internalDate) {
        mutableStateOf(formatDateForDisplay(internalDate))
    }

    LaunchedEffect(ideaId) {
        isLoading = true
        if (ideaId == null) {
            isLoading = false
        } else {
            try {
                idea = supabaseHelper.getIdeaById(ideaId, userId)
                idea?.let {
                    title = it.title ?: ""
                    description = it.description ?: ""
                    internalDate = it.date ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                }
                isLoading = false
            } catch (e: Exception) {
                error = "Ошибка при загрузке данных: ${e.message}"
                isLoading = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Новая идея" else "Идея",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            color = Primary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Primary
                        )
                    }
                },
                actions = {
                    if (ideaId != null && !isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                painter = painterResource(R.drawable.pencil_ic),
                                contentDescription = "Редактировать",
                                tint = Primary
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (ideaId != null || isEditing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (ideaId != null && !isEditing) {
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val success = supabaseHelper.deleteIdea(ideaId)
                                        withContext(Dispatchers.Main) {
                                            if (success) {
                                                onIdeaSaved()
                                            } else {
                                                error = "Не удалось удалить идею"
                                            }
                                        }
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            error = "Ошибка при удалении идеи: ${e.message}"
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Red
                            ),
                            border = BorderStroke(1.dp, Red),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            Spacer(Modifier.width(8.dp))
                            Text("Удалить")
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    Button(
                        onClick = {
                            if (isEditing) {
                                val currentIdea = Idea(
                                    id = ideaId,
                                    userId = userId,
                                    title = title,
                                    description = description,
                                    date = internalDate
                                )

                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        if (ideaId == null) {
                                            supabaseHelper.insertIdea(currentIdea)
                                        } else {
                                            supabaseHelper.updateIdea(currentIdea)
                                        }
                                        onIdeaSaved()
                                    } catch (e: Exception) {
                                        error = "Ошибка при сохранении идеи: ${e.message}"
                                    }
                                }
                            } else {
                                isEditing = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = White
                        ),
                        modifier = if (ideaId != null && !isEditing) Modifier.weight(1f) else Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = if (isEditing) "Сохранить" else "Редактировать"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isEditing) "Сохранить" else "Редактировать")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {
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
            } else if (!isEditing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = idea?.title ?: "",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_bold)),
                            color = DarkBlue
                        )
                    )

                    if (!idea?.description.isNullOrBlank()) {
                        Text(
                            text = idea?.description ?: "",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                color = DarkBlue
                            )
                        )
                    }

                    Text(
                        text = "Дата: ${formatDateForDisplay(idea?.date ?: "")}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = Gray1
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                // Edit mode
                Column(modifier = Modifier.fillMaxSize()) {
                    // Title field
                    Text(
                        text = "Наименование идеи",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Введите название идеи") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description field
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("О чём ваша идея?") },
                        singleLine = false,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date field
                    Text(
                        text = "Дата",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = displayDate,
                        onValueChange = { displayDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("yyyy-MM-dd") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }
    }
}
private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
private val displayDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

private fun formatDateForDisplay(dateString: String): String {
    return try {
        val date = inputDateFormat.parse(dateString)
        displayDateFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}

private fun formatDateForStorage(dateString: String): String {
    return try {
        val date = displayDateFormat.parse(dateString)
        inputDateFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}