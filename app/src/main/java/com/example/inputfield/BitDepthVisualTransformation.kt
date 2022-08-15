package com.example.inputfield

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class BitDepthVisualTransformation : VisualTransformation {

    private fun formatWithBitDepth(source: String): String {

        val currentLocale: Locale = Locale.getDefault()
        val otherSymbols = DecimalFormatSymbols(currentLocale)
        otherSymbols.decimalSeparator = '.'
        otherSymbols.groupingSeparator = '\''
        val decimalFormat = DecimalFormat("#,###.####", otherSymbols)

        return decimalFormat.format(source.toDouble())
    }

    private fun checkIsDouble(source: String): Boolean {
        val valDouble: Double? = source.toDoubleOrNull()
        return valDouble != null
    }

    override fun filter(text: AnnotatedString): TransformedText {

        val originalText = text.text
        val formattedText = formatWithBitDepth(text.text)

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                if (checkIsDouble(originalText)) {
                    val separators = formattedText.count { it == '\'' }
                    if (separators == 0) return offset
                    else
                    {
                        val firstSeparatorIndex = formattedText.indexOfFirst { it == '\'' }
                        val isPositive = originalText.toDouble() >= 0
                        val negativeShift = if (isPositive) 0 else 1
                        if (negativeShift == 1 && offset == 0) return offset
                        if (separators == 1) {
                            return when (offset) {
                                0 + negativeShift -> offset
                                1 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 1 else offset
                                2 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 1 else offset
                                else -> offset + 1
                            }
                        } else if (separators == 2) {
                            return when (offset) {
                                0 + negativeShift -> offset
                                1 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 1 else offset
                                2 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 1 else offset
                                3 + negativeShift -> offset + 1
                                4 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 2 else offset + 1
                                5 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 2 else offset + 1
                                else -> offset + 2
                            }
                        } else if (separators == 3) {
                            return when (offset) {
                                0 + negativeShift -> offset
                                1 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 1 else offset
                                2 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 1 else offset
                                3 + negativeShift -> offset + 1
                                4 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 2 else offset + 1
                                5 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 2 else offset + 1
                                6 + negativeShift -> offset + 2
                                7 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset + 3 else offset + 2
                                8 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset + 3 else offset + 2
                                else -> offset + 3
                            }
                        }
                    }
                }
                return offset
            }

                override fun transformedToOriginal(offset: Int): Int {
                    if (checkIsDouble(originalText)) {
                        val separators = formattedText.count { it == '\'' }
                        if (separators == 0) return offset
                        else
                        {
                            val firstSeparatorIndex = formattedText.indexOfFirst { it == '\'' }
                            val isPositive = originalText.toDouble() >= 0
                            val negativeShift = if (isPositive) 0 else 1
                            if (offset == 0) return offset
                            if (negativeShift == 1 && offset == 1) return offset
                            if (separators == 1) {
                                return when (offset) {
                                    1 + negativeShift -> offset
                                    2 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 1 else offset
                                    3 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 1 else offset
                                    else -> offset - 1
                                }
                            } else if (separators == 2) {
                                return when (offset) {
                                    1 + negativeShift -> offset
                                    2 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 1 else offset
                                    3 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 1 else offset
                                    4 + negativeShift -> offset - 1
                                    5 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 2 else offset - 1
                                    6 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 2 else offset - 1
                                    else -> offset - 2
                                }
                            } else if (separators == 3) {
                                return when (offset) {
                                    1 + negativeShift -> offset
                                    2 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 1 else offset
                                    3 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 1 else offset
                                    4 + negativeShift -> offset - 1
                                    5 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 2 else offset - 1
                                    6 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 2 else offset - 1
                                    7 + negativeShift -> offset - 2
                                    8 + negativeShift -> if (firstSeparatorIndex == 1 + negativeShift) offset - 3 else offset - 2
                                    9 + negativeShift -> if (firstSeparatorIndex <= 2 + negativeShift) offset - 3 else offset - 2
                                    else -> offset - 3
                                }
                            }
                        }
                    }
                    return offset
                }
            }

            return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
            )
        }
}
