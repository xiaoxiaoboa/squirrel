package com.squirrel.utils.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.squirrel.utils.BottomNavItem

@Composable
fun NavigationBottomBar(currentRoute: String, handleNavigation: (route: String) -> Unit) {

    val items = listOf(
        BottomNavItem.Home,
//        BottomNavItem.Accounts,
//        BottomNavItem.Statistics,
        BottomNavItem.Setting
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
        items.forEach { item ->
            AddItem(currentRoute = currentRoute, screen = item) {
                if (item.route != currentRoute) handleNavigation(item.route)
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    currentRoute: String,
    screen: BottomNavItem,
    handleSelect: () -> Unit,
) {
    NavigationBarItem(
        label = {
            Text(text = screen.tittle)
        },
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = screen.icon),
                contentDescription = null,
            )
        },
        selected = screen.route == currentRoute,
        alwaysShowLabel = true,
        onClick = {
            handleSelect()
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            indicatorColor = MaterialTheme.colorScheme.onTertiary
        )
    )
}

