package com.squirrel.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squirrel.utils.Accounts
import com.squirrel.utils.Categories
import com.squirrel.utils.formatDouble
import com.squirrel.utils.types.Day
import com.squirrel.utils.types.Item
import com.squirrel.utils.ui.SquirrelAlertDialog


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeItem(
    category: Int,
    item: Item,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
) {
    Row(Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .combinedClickable(onClick = { onClick() }, onLongClick = {
            onLongClick()
        }), verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier.padding(horizontal = 15.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(10.dp), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = ImageVector.vectorResource(id = Categories[category].icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = Categories[item.category].name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row {
                        Text(
                            text = item.time,
                            color = MaterialTheme.colorScheme.secondary,
                        )

                        Text(
                            text = if (item.remark.isNotEmpty()) " | ${item.remark}" else "",
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "¥${formatDouble(item.exp)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = Accounts[item.account].icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }


    }
}