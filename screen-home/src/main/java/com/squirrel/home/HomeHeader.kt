package com.squirrel.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squirrel.utils.Constants
import com.squirrel.utils.formatDouble
import com.squirrel.utils.formatMoneyDisplay
import com.squirrel.utils.types.Date

@Composable
fun HomeHeader(
    date: Date,
    monthTotalExp: Double,
    toggleDatePickerShow: (show: Boolean) -> Unit,
    toggleCategoryModalShow: (show: Boolean) -> Unit,
    toggleSettingModalShow: (show: Boolean) -> Unit
) {
    val money = formatDouble(monthTotalExp)
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Squirrel",
                fontFamily = FontFamily(Font(R.font.kalam_bold)),
                fontSize = 36.sp,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                HomeHeaderBtn(icon = R.drawable.calendar, iconSize = 35) {
                    toggleDatePickerShow(true)
                }
                Spacer(modifier = Modifier.size(10.dp))
                HomeHeaderBtn(icon = R.drawable.user) {
                    toggleSettingModalShow(true)
                }
                Spacer(modifier = Modifier.size(10.dp))
                HomeHeaderBtn(icon = R.drawable.plus) {
                    toggleCategoryModalShow(true)
                }
            }

        }
        Spacer(modifier = Modifier.size(20.dp))
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${date.month}月",
                    fontSize = 26.sp,
                    modifier = Modifier.alignBy(FirstBaseline)
                )
                Text(text = "支出", modifier = Modifier.alignBy(FirstBaseline))
            }
            Text(
                text = "¥${money}",
                fontWeight = FontWeight.Bold,
                fontSize = formatMoneyDisplay(money).sp
            )
        }
    }
}

@Composable
fun HomeHeaderBtn(icon: Int, iconSize: Int = 35, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(CircleShape)
            .padding(4.dp)
            .clickable { onClick() }) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

