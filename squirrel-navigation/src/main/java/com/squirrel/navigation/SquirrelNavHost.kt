package com.squirrel.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.squirrel.drive.aliyun.AliYunScreen
import com.squirrel.home.HomeScreen
import com.squirrel.setting.SettingScreen
import com.squirrel.utils.NavRoutes


@Composable
fun SquirrelNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController = navController,
        startDestination = NavRoutes.homeRoute,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        /* 主页 */
        composable(route = NavRoutes.homeRoute, enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }) {
            HomeScreen(navController = navController)
        }

        /* 统计 */
        composable(route = NavRoutes.analysisRoute, enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }) {
            AnalysisScreen()
        }

        /* 设置 */
        composable(route = NavRoutes.setting, enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        }
        ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoutes.setting)
            }
            SettingScreen(navController = navController, parentEntry = parentEntry)
        }

        /* 阿里云登录webview */
        composable(
            route = "${NavRoutes.aliyunWebView}/{base64Url}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(300)
                )
            },
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(NavRoutes.setting)
            }
            val url = entry.arguments?.getString("base64Url")
            url?.let {
                AliYunScreen(base64Url = it, parentEntry = parentEntry)
            }
        }

    }
}