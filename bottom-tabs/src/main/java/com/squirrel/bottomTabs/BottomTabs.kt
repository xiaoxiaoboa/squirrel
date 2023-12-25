package com.squirrel.bottomTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.squirrel.utils.NavRoutes

@Composable
fun BottomTabs(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        BottomNavButton(
            icon = R.drawable.bill,
            text = "账单",
            modifier = Modifier.weight(1f),
            selected = navBackStackEntry?.destination?.route == NavRoutes.homeRoute
        ) {
            navController.navigate(route = NavRoutes.homeRoute) {
                popUpTo(NavRoutes.homeRoute)
                launchSingleTop = true
            }
        }
        BottomNavButton(
            icon = R.drawable.analysis,
            text = "统计",
            modifier = Modifier.weight(1f),
            selected = navBackStackEntry?.destination?.route == NavRoutes.analysisRoute
        ) {
            navController.navigate(route = NavRoutes.analysisRoute) {
                popUpTo(NavRoutes.analysisRoute)
                launchSingleTop = true
            }
        }
    }
}

@Composable
fun BottomNavButton(
    icon: Int, text: String, modifier: Modifier = Modifier, selected: Boolean, onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier
            .height(65.dp)
            .clickable(
                interactionSource = interactionSource, indication = null
            ) {
                onClick()
            }) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = null,
                Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(2.dp))
            if (selected) Text(text = text)
        }
    }


}

