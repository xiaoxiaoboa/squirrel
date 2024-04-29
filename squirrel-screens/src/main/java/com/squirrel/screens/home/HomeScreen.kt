package com.squirrel.screens.home

import com.squirrel.utils.ui.NavigationBottomBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.squirrel.screens.transaction.TransactionViewModel
import com.squirrel.utils.Constants
import com.squirrel.utils.NavRoutes
import com.squirrel.utils.formatDateForCurrentTime
import com.squirrel.utils.formatDateForYearMonthDay
import com.squirrel.utils.formatDouble
import com.squirrel.utils.ui.SquirrelDatePicker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    transactionViewModel: TransactionViewModel
) {

    Scaffold(
        modifier = Modifier.padding(
            top = 15.dp
        ),
        bottomBar = {
            NavigationBottomBar(NavRoutes.HOME_ROUTE) {
                navController.navigate(route = it) {
                    popUpTo(NavRoutes.HOME_ROUTE) {
                        inclusive = true
                    }
                }
            }
        },
        topBar = {
            HomeHeader(
                modifier = Modifier.padding(horizontal = 10.dp),
                date = homeViewModel.date,
                monthTotalExp = homeViewModel.monthTotalExp,
                toggleDatePickerShow = homeViewModel::toggleDatePickerShow,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val timestamp = System.currentTimeMillis()
                    transactionViewModel.toggleTransactionTime(
                        formatDateForCurrentTime(
                            millis = timestamp,
                            formatter = Constants.HOUR_MINUTE
                        )
                    )
                    transactionViewModel.toggleTransactionTimestamp(timestamp)
                    navController.navigate(route = "${NavRoutes.TRANSACTION_ROUTE}/添加新交易")
                },
                modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                homeViewModel.daysWithItems.forEach { dayWithItems ->
                    stickyHeader(key = dayWithItems.day.dayId) {
                        DayItem(day = dayWithItems.day)
                    }

                    items(items = dayWithItems.items, key = { it.itemId }) { item ->
                        HomeItem(
                            category = item.category,
                            item = item,
                        ) {
                            /* 点击item后，将信息存储到transiactionViewmodel中 */
                            transactionViewModel.toggleTransaction(item)
                            transactionViewModel.toggleDay(dayWithItems.day)
                            transactionViewModel.toggleAmount(formatDouble(item.amount))
                            transactionViewModel.toggleAccountIndex(item.account)
                            transactionViewModel.toggleRemark(item.remark)
                            transactionViewModel.toggleTransactionCategory(item.category)
                            transactionViewModel.toggleTransactionDate(
                                year = item.year,
                                month = item.month,
                                day = item.day,
                                formatter = Constants.YEAR_MONTH_DAY,
                            )
                            transactionViewModel.toggleTransactionTime(
                                formatDateForCurrentTime(
                                    millis = item.timestamp,
                                    formatter = Constants.HOUR_MINUTE
                                )
                            )
                            navController.navigate("${NavRoutes.TRANSACTION_ROUTE}/交易详情")
                        }
                    }
                }
            }
        }
    }

    if (homeViewModel.datePickerShow) {
        SquirrelDatePicker(
            onDismiss = homeViewModel::toggleDatePickerShow,
        ) {
            homeViewModel.toggleDate(formatDateForYearMonthDay(it))
            homeViewModel.toggleDatePickerShow(false)

        }
    }
}
