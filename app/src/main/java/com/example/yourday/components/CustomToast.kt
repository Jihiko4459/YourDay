package com.example.yourday.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yourday.R
import com.example.yourday.ui.theme.Green
import com.example.yourday.ui.theme.Red
import com.example.yourday.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ToastType {
    SUCCESS, ERROR
}

enum class ToastDuration(val timeMillis: Long) {
    SHORT(2000L),
    LONG(3500L)
}

@Composable
fun CustomToast(
    message: String,
    type: ToastType = ToastType.SUCCESS,
    duration: ToastDuration = ToastDuration.SHORT,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        ToastType.SUCCESS -> Green
        ToastType.ERROR -> Red
    }

    val iconRes = when (type) {
        ToastType.SUCCESS -> R.drawable.check_circle2
        ToastType.ERROR -> R.drawable.check_circle
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(size = 8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 2.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Toast icon",
                contentScale = ContentScale.None
            )
            Text(
                text = message,
                modifier = modifier.padding(16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                    color = White
                )
            )
        }
    }
}

@Composable
fun GlobalToast() {
    AnimatedVisibility(
        visible = ToastManager.showToast,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 55.dp)
    ) {
        if (ToastManager.showToast) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomCenter)
            ) {
                CustomToast(
                    message = ToastManager.toastMessage,
                    type = ToastManager.toastType,
                    duration = ToastManager.toastDuration
                )
            }
        }
    }
}

object ToastManager {
    var showToast by mutableStateOf(false)
    var toastMessage by mutableStateOf("")
    var toastType by mutableStateOf(ToastType.SUCCESS)
    var toastDuration by mutableStateOf(ToastDuration.SHORT)

    fun show(
        message: String,
        type: ToastType = ToastType.SUCCESS,
        duration: ToastDuration = ToastDuration.SHORT
    ) {
        toastMessage = message
        toastType = type
        toastDuration = duration
        showToast = true

        CoroutineScope(Dispatchers.Main).launch {
            delay(duration.timeMillis)
            showToast = false
        }
    }
}

@Composable
fun ManagedCustomToast() {
    AnimatedVisibility(
        visible = ToastManager.showToast,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        if (ToastManager.showToast) {
            CustomToast(
                message = ToastManager.toastMessage,
                type = ToastManager.toastType
            )
        }
    }
}

// Helper functions for backward compatibility
@Composable
fun CustomToastGreen(
    message: String,
    duration: ToastDuration = ToastDuration.SHORT,
    modifier: Modifier = Modifier
) {
    CustomToast(
        message = message,
        type = ToastType.SUCCESS,
        duration = duration,
        modifier = modifier
    )
}

@Composable
fun CustomToastRed(
    message: String,
    duration: ToastDuration = ToastDuration.SHORT,
    modifier: Modifier = Modifier
) {
    CustomToast(
        message = message,
        type = ToastType.ERROR,
        duration = duration,
        modifier = modifier
    )
}

@Preview
@Composable
fun CustomToastPreview() {
    Column {
        CustomToast(
            message = "Успешная операция",
            type = ToastType.SUCCESS
        )
        CustomToast(
            message = "Ошибка операции",
            type = ToastType.ERROR
        )
    }
}