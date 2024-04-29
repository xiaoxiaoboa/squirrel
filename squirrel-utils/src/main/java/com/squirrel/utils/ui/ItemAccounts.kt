package com.squirrel.utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squirrel.utils.types.Account

@Composable
fun ItemAccounts(
    accounts: List<Account> = emptyList(),
    accountIndex: Int = 1,
    toggleAccount: (account: Int) -> Unit,
) {
    Row(Modifier.padding(horizontal = 20.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(accounts) { index, item ->
                Row(
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            1.dp,
                            if (accountIndex == index + 1) Color.Transparent else MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(20.dp)
                        )
                        .background(if (accountIndex == index + 1) MaterialTheme.colorScheme.onTertiary else Color.Transparent)
                        .clickable { toggleAccount(index + 1) }) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 14.sp,
                            color = if (accountIndex == index + 1) Color.White else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}