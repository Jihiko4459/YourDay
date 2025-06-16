package com.example.yourday.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Форматирование ввода как ДД.ММ.ГГГГ
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            when (i) {
                1, 3 -> out += "."
            }
        }

        val numberOffsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    0 -> 0
                    1 -> 1
                    2 -> 3
                    3 -> 4
                    4 -> 6
                    5 -> 7
                    6 -> 8
                    7 -> 9
                    else -> 10
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    0 -> 0
                    1 -> 1
                    3 -> 2
                    4 -> 3
                    6 -> 4
                    7 -> 5
                    8 -> 6
                    9 -> 7
                    else -> 8
                }
            }
        }

        return TransformedText(
            AnnotatedString(out),
            numberOffsetMapping
        )
    }
}