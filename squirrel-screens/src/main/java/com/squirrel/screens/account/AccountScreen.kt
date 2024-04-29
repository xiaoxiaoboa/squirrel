package com.squirrel.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.squirrel.utils.NavRoutes
import com.squirrel.utils.ui.NavigationBottomBar

@Composable
fun AccountScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Scaffold(modifier = modifier.padding(top = 15.dp), bottomBar = {
        NavigationBottomBar(currentRoute = NavRoutes.ACCOUNT_ROUTE) {
            navController.navigate(route = it) {
                popUpTo(route = NavRoutes.ACCOUNT_ROUTE) {
                    inclusive = true
                }
            }
        }
    }) {
        Column(modifier = modifier.padding(it)) {

        }
    }
}