package com.squirrel.screens.home.modal

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetModal(
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    onClose: (show: Boolean) -> Unit,
    content: @Composable () -> Unit
) {

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.onSecondary,
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onClose(false)
            }
        },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }) {
        content()
    }

}