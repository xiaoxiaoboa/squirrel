package com.squirrel.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squirrel.utils.Constants
import com.squirrel.utils.formatDate
import com.squirrel.utils.formatDouble
import com.squirrel.utils.getDayOfWeek
import com.squirrel.utils.types.Day

@Composable
fun DayItem(day: Day) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 8.dp,end = 8.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${
                formatDate(
                    year = day.year,
                    month = day.month,
                    day = day.day,
                    formatter = Constants.MONTH_DAY
                )
            } ${
                getDayOfWeek(
                    year = day.year,
                    month = day.month,
                    day = day.day
                )
            }", color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "支出${formatDouble(day.dayExpenditure)}",
            color = MaterialTheme.colorScheme.primary
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
}
