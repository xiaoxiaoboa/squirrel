package com.squirrel.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.squirrel.utils.Categories
import com.squirrel.utils.types.Category
import com.squirrel.utils.ui.MyKeyBoard
import com.squirrel.utils.ui.TransactionCategory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCategories(
    handleClose: () -> Unit,
    handleClick: (category: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.onSecondary,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                handleClose()
            }
        }) {
        Column(
            Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(Categories) { index, _ ->
                    TransactionCategory(
                        modifier = Modifier.size(28.dp),
                        enabled = true,
                        category = Categories[index]
                    ) {
                        handleClick(index)
                        coroutineScope.launch {
                            sheetState.hide()
                            handleClose()
                        }
                    }
                }
            }
        }
        Spacer(
            Modifier.navigationBarsPadding()
        )
    }
}

