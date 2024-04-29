package com.squirrel.utils

import androidx.compose.material.icons.Icons

sealed class BottomNavItem(var tittle: String, var icon: Int, var route: String) {
    data object Home : BottomNavItem(
        tittle = "主页",
        icon = R.drawable.homescreen,
        route = NavRoutes.HOME_ROUTE
    )

    data object Accounts : BottomNavItem(
        tittle = "账户",
        icon = R.drawable.account,
        route = NavRoutes.ACCOUNT_ROUTE
    )

    data object Statistics : BottomNavItem(
        tittle = "统计",
        icon = R.drawable.statistics,
        route = NavRoutes.STATISTICS_ROUTE
    )
    data object Setting : BottomNavItem(
        tittle = "设置",
        icon = R.drawable.more_horiz,
        route = NavRoutes.SETTING_ROUTE
    )

}