package com.squirrel.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.squirrel.utils.Categories
import com.squirrel.utils.types.Category


@Composable
fun CategoryLayout(
    toggleAdditionItemModalShow: (show: Boolean) -> Unit,
    toggleItemCategory: (category: Int) -> Unit,
) {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(Categories) { index, _ ->
                CategoryItem(
                    category = Categories[index],
                ) {
                    toggleItemCategory(index)
                    toggleAdditionItemModalShow(true)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        Modifier.clickable(interactionSource = interactionSource, indication = null) {
            onClick()
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .border(1.dp, MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                .padding(10.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = ImageVector.vectorResource(id = category.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryContainer
            )
        }
        Text(
            text = category.name, color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}
