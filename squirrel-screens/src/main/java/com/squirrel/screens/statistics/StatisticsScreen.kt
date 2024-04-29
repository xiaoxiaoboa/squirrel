package com.squirrel.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.squirrel.utils.NavRoutes
import com.squirrel.utils.ui.NavigationBottomBar

@Composable
fun StatisticsScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Scaffold(modifier = modifier.padding(top = 15.dp), bottomBar = {
        NavigationBottomBar(currentRoute = NavRoutes.STATISTICS_ROUTE) {
            navController.navigate(route = it) {
                popUpTo(route = NavRoutes.STATISTICS_ROUTE) {
                    inclusive = true
                }
            }
        }
    }) {
        Column(modifier = modifier.padding(it)) {
            Text(text = "开发中...")
        }
    }

}