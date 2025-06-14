
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yourday.R
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1
import java.time.Instant
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val currentDate = remember {  Instant.now().toEpochMilli() }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate
    )

    // Форматирование даты
    fun formatDate(millis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val year = calendar.get(Calendar.YEAR)
        return "$day.$month.$year"
    }

    Box(
        modifier = modifier
            .background(Purple1, RoundedCornerShape(8.dp))
            .clickable { showDatePicker = true }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.calendar), // Убедитесь, что этот ресурс существует
                contentDescription = "Выбрать дату",
                tint = Primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Выберите дату",
                    color = DarkBlue,
                    fontSize = 12.sp
                )
                Text(
                    text = formatDate(datePickerState.selectedDateMillis ?: currentDate),
                    color = DarkBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // При первом отображении вызываем callback с текущей датой
    LaunchedEffect(Unit) {
        onDateSelected(currentDate)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
