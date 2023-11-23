package com.squirrel.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.squirrel.drive.aliyun.AliYunScreen
import com.squirrel.home.HomeScreen
import com.squirrel.utils.NavRoutes


@Composable
fun SquirrelNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.homeRoute,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = NavRoutes.homeRoute) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoutes.homeRoute)
            }
            HomeScreen(navController = navController, parentEntry = parentEntry)
        }
        composable(route = NavRoutes.analysisRoute) {
            AnalysisScreen()
        }
        composable(
            route = "${NavRoutes.aliyunWebView}/{base64Url}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(NavRoutes.homeRoute)
            }
            val url = entry.arguments?.getString("base64Url")
            url?.let {
                AliYunScreen(base64Url = it, parentEntry = parentEntry)
            }
        }

    }
}