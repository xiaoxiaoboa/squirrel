package com.squirrel.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.squirrel.drive.aliyun.AliYunScreen
import com.squirrel.screens.account.AccountScreen
import com.squirrel.screens.home.HomeScreen
import com.squirrel.screens.home.HomeViewModel
import com.squirrel.screens.setting.SettingScreen
import com.squirrel.screens.statistics.StatisticsScreen
import com.squirrel.screens.transaction.TransactionScreen
import com.squirrel.screens.transaction.TransactionViewModel
import com.squirrel.utils.NavRoutes


@Composable
fun SquirrelNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    NavHost(navController = navController,
        startDestination = NavRoutes.HOME_ROUTE,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        /* 主页 */
        composable(route = NavRoutes.HOME_ROUTE) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                transactionViewModel = transactionViewModel
            )
        }

        /* 交易页 */
        navItem(route = "${NavRoutes.TRANSACTION_ROUTE}/{tittle}") {
            val tittle = it.arguments?.getString("tittle")
            tittle?.let { str ->
                TransactionScreen(
                    tittle = str,
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }
        }


        /* 统计 */
        composable(route = NavRoutes.STATISTICS_ROUTE) {
            StatisticsScreen(navController = navController)
        }
        /* 账户 */
        composable(route = NavRoutes.ACCOUNT_ROUTE) {
           AccountScreen(navController = navController)
        }

        /* 设置 */
        composable(route = NavRoutes.SETTING_ROUTE) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoutes.SETTING_ROUTE)
            }
            SettingScreen(navController = navController, parentEntry = parentEntry)
        }

        /* 阿里云登录webview */
        navItem(route = "${NavRoutes.ALIYUN_WEBVIEW_ROUTE}/{base64Url}") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoutes.SETTING_ROUTE)
            }
            val url = it.arguments?.getString("base64Url")
            url?.let { str ->
                AliYunScreen(base64Url = str, parentEntry = parentEntry)
            }
        }

    }
}


fun NavGraphBuilder.navItem(
    route: String,
    callBack: @Composable (it: NavBackStackEntry) -> Unit
) {
    composable(route = route, enterTransition = {
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
        callBack(it)
    }

}

