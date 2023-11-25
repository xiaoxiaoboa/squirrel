package com.squirrel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.squirrel.bottomTabs.BottomTabs
import com.squirrel.navigation.SquirrelNavHost

@Composable
fun SquirrelApp() {
    val navController = rememberNavController()
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SquirrelNavHost(navController = navController)
        }
//        BottomTabs(navController = navController)
    }
}