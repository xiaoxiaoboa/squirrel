package com.squirrel.utils.ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquirrelDatePicker(
    onDismiss: (show: Boolean) -> Unit,
    onConfirm: (it: Long) -> Unit
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    DatePickerDialog(
        onDismissRequest = {
            onDismiss(false)
        }, confirmButton = {
            DatePickerButton(text = "确定", onClick = {
                datePickerState.selectedDateMillis?.let {
                    onConfirm(it)
                }
            })
        }, dismissButton = {
            DatePickerButton(text = "取消", onClick = {
                onDismiss(false)
            })
        }) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = MaterialTheme.colorScheme.onTertiary,
                todayDateBorderColor = MaterialTheme.colorScheme.onTertiary,
                selectedDayContentColor = Color.White
            ),
        )
    }
}

@Composable
fun DatePickerButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = { onClick() }) {
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}

