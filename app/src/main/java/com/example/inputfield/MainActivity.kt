package com.example.inputfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inputfield.ui.theme.InputFieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InputFieldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Начальное значение для поля
                    val initialValue = 123.45
                    // Диапазон допустимых значений
                    val minBorder = -9000000000.00
                    val maxBorder = 9000000000.00

                    Column(modifier = Modifier.fillMaxWidth()) {
                        DrawInstruction(initialValue, minBorder, maxBorder)
                        DrawInputAndButton(initialValue, minBorder, maxBorder)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawInstruction(initialValue: Double, minBorder: Double, maxBorder: Double) {
    Column {
        Text(text = "Введите десятичное число:")
        Text(
            text = "Начальное значение: $initialValue",
            modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "Ввести можно только число. \nОно может иметь знак и десятичную точку",
            modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "Диапазон значений: от $minBorder до $maxBorder",
            modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "Цифры слева от десятичной точки сгруппированы по 3 знака",
            modifier = Modifier.padding(start = 16.dp))
    }

}

@Composable
fun DrawInputAndButton(initialValue: Double, minBorder: Double, maxBorder: Double) {

    var fieldValue by remember { mutableStateOf(initialValue.toString()) }

    Column(modifier = Modifier.padding(16.dp)) {

        TextField(
            value = fieldValue,
            onValueChange = {
                // variant 1: if dot is last - just remove it
                if (checkIsDouble(it) && checkIsInRange(it, minBorder, maxBorder)) fieldValue = it.removeSuffix(".")

                // variant 2: if dot is last - add some decimal digit (but with 0 - doesn't work)
                /*if (checkIsDouble(it) && checkIsInRange(it, minBorder, maxBorder)) {
                    if (checkIfDotIfLast(it)) fieldValue = it.replace(".", ".9", false)
                    else fieldValue = it
               }*/
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = BitDepthVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text("Сохранить")
        }
    }
}

private fun checkIfDotIfLast(currentValue: String): Boolean {
    val lastSymbolIndex = currentValue.lastIndex
    return if (lastSymbolIndex != -1) {
        currentValue[lastSymbolIndex] == '.'
    }
    else false
}


private fun checkIsDouble(source: CharSequence): Boolean {
    val pattern = Regex("[-]?[0-9]+[.]?[0-9]{0,4}")
    return pattern.matches(source)
}

private fun checkIsInRange(source: String, minBorder: Double, maxBorder: Double): Boolean {
    val valDouble: Double? = source.toDoubleOrNull()
    if (valDouble === null) return false
    return (valDouble in minBorder..maxBorder)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InputFieldTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            DrawInstruction(initialValue = 123.123, -3000.00, 3000.00)
            DrawInputAndButton(initialValue = 123.123, -3000.00, 3000.00)
        }
    }
}