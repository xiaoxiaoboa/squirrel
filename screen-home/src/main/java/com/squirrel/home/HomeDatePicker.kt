package com.squirrel.home

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDatePicker(
    toggleDatePickerShow: (show: Boolean) -> Unit,
    onConfirm: (it: Long) -> Unit
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    DatePickerDialog(onDismissRequest = {
        toggleDatePickerShow(false)
    }, confirmButton = {
        DatePickerButton(text = "确定", onClick = {
            datePickerState.selectedDateMillis?.let {
                onConfirm(it)
            }
        })
    }, dismissButton = {
        DatePickerButton(text = "取消", onClick = {
            toggleDatePickerShow(false)
        })
    }) {
        DatePicker(
            state = datePickerState,
        )
    }
}

@Composable
fun DatePickerButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = { onClick() }) {
        Text(text = text, color = MaterialTheme.colorScheme.primary)
    }
}

