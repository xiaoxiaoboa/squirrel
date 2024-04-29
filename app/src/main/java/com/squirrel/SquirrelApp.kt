package com.squirrel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.squirrel.navigation.SquirrelNavHost
import com.squirrel.screens.applock.AppLockScreen

@Composable
fun SquirrelApp() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (rootViewModel.appLocked) {
            null -> {}
            false -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    SquirrelNavHost(
                        navController = navController,
                    )

                }
            }

            true -> {
                AppLockScreen(unLock = rootViewModel::unLockApp)
            }
        }

    }
}