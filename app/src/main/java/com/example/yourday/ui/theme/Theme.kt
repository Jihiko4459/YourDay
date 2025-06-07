import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.yourday.ui.theme.Black
import com.example.yourday.ui.theme.DarkBlue
import com.example.yourday.ui.theme.Gray1
import com.example.yourday.ui.theme.Gray2
import com.example.yourday.ui.theme.Primary
import com.example.yourday.ui.theme.Purple1
import com.example.yourday.ui.theme.Purple4
import com.example.yourday.ui.theme.Red
import com.example.yourday.ui.theme.Typography
import com.example.yourday.ui.theme.White

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    background = White,
    onBackground = Gray1,
    surface = Purple1,
    onSurface = DarkBlue,
    error = Red,
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = Black,
    background = Black,
    onBackground = Gray2,
    surface = Purple4,
    onSurface = White,
    error = Red,
)

@Composable
fun YourDayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}