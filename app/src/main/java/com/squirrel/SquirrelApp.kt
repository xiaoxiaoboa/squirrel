package com.squirrel

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.squirrel.navigation.AppLockScreen
import com.squirrel.navigation.SquirrelNavHost

@Composable
fun SquirrelApp() {
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()

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

            when (rootViewModel.appLocked) {
                null -> {}
                false -> {
                    SquirrelNavHost(navController = navController)
//                    BottomTabs(navController = navController)

                }

                true -> {
                    AppLockScreen(unLock = rootViewModel::unLockApp)
                }
            }
        }
    }
}