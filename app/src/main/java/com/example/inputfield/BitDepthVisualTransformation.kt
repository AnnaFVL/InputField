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
        var otherSymbols: DecimalFormatSymbols = DecimalFormatSymbols(currentLocale)
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator('\'');
        var df: DecimalFormat = DecimalFormat("#,###.####", otherSymbols);

        return df.format(source.toDouble())
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
                    return when (offset) {
                        /*
                        0, 1, 2 -> offset
                        3, 4, 5, 6, 7 -> if (separators == 1) offset + 1 else offset
                        8, 9, 10, 11 -> if (separators == 2) offset + 2 else offset + 1
                        else -> if (separators == 3) offset + 3 else offset + 2

                        8, 7 -> offset - 2
                        6 -> if (commas == 1) 5 else 4
                        5 -> if (commas == 1) 4 else if (commas == 2) 3 else offset
                        4, 3 -> if (commas >= 1) offset - 1 else offset
                        2 -> if (commas == 2) 1 else offset
                        else -> offset

                        */
                        /*
                        0, 1 -> offset
                        2, 3 -> if (separators >= 1) offset + 1 else offset
                        4, 5 -> if (separators >= 2) offset + 2 else offset + 1
                        6, 7, 8 -> if (separators == 3) offset + 3 else offset + 2
                        else -> 9*/
/*
                        0, 1 -> offset
                        2, 3, 4 -> if (separators >= 1) offset + 1 else offset
                        5, 6 -> if (separators >= 2) offset + 2 else offset + 1
                        7, 8 -> if (separators == 3) offset + 3 else offset + 2
                        else -> 9*/

                        0, 1, 2 -> offset
                        3, 4, 5 -> offset + 1
                        6, 7, 8 -> offset + 2
                        else -> offset + 3

                    }
                }
                    return offset
            }

                override fun transformedToOriginal(offset: Int): Int {
                    if (checkIsDouble(originalText)) {
                        val separators = formattedText.count { it == '\'' }
                        return when (offset) {
/*
                            10, 9 -> offset - 3
                            8, 7 -> offset - 2
                            5 -> if (separators == 1) 4 else if (separators == 2) 3 else offset
                            4, 3 -> if (separators >= 1) offset - 1 else offset
                            2 -> if (separators == 2) 1 else offset
                            else -> offset*/
/*
                            10 -> offset - 3
                            9, 8 -> offset - 2
                            7, 6 -> if (separators == 1) 4 else if (separators == 2) 3 else offset
                            5, 4 -> if (separators >= 1) offset - 1 else offset
                            2, 3 -> if (separators == 2) 1 else offset
                            else -> offset*/

                            10, 9 -> offset - 3
                            8, 7, 6 -> offset - 2
                            5, 4, 3 -> offset - 1
                            else -> offset
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
