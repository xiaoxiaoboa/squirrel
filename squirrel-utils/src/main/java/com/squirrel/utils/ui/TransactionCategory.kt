package com.squirrel.utils.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.squirrel.utils.types.Category

@Composable
fun TransactionCategory(
    modifier: Modifier,
    category: Category,
    enabled: Boolean = false,
    showCategoryName: Boolean = true,
    handleClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clickable(enabled = enabled) {
                handleClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.padding(start = 4.dp, top = 4.dp, end = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(10.dp), contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = modifier,
                    imageVector = ImageVector.vectorResource(id = category.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (showCategoryName)
                Text(text = category.name, color = MaterialTheme.colorScheme.primary)
        }
    }
}