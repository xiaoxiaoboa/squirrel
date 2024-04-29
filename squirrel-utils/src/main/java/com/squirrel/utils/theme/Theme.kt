package com.squirrel.utils.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.squirrel.utils.LocalSnackBarHostState

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    secondary = SecondaryColorDark,
    onSecondary = OnSecondaryColorDark,
    secondaryContainer = SecondaryContainerDark,
    tertiaryContainer = TertiaryContainerDark,
    tertiary = CustomRippleColorDark,
    onTertiary = OnTertiaryColorDark,
    background = BackgroundColorDark,
    onBackground = OnBackgroundColorDark,
    onPrimaryContainer = onPrimaryContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    secondary = SecondaryColorLight,
    onSecondary = OnSecondaryColorLight,
    secondaryContainer = SecondaryContainerLight,
    tertiaryContainer = TertiaryContainerLight,
    tertiary = CustomRippleColorLight,
    onTertiary = OnTertiaryColorLight,
    background = BackgroundColorLight,
    onBackground = OnBackgroundColorLight,
    onPrimaryContainer = onPrimaryContainerLight
)

@Composable
private fun animateColor(targetValue: Color) =
    animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 700), label = ""
    ).value

@Composable
fun ColorScheme.switch() = copy(
    background = animateColor(targetValue = background),
    primary = animateColor(targetValue = primary),
    secondary = animateColor(targetValue = secondary),
    onSecondary = animateColor(targetValue = onSecondary),
    secondaryContainer = animateColor(targetValue = secondaryContainer),
    tertiaryContainer = animateColor(targetValue = tertiaryContainer),
    onTertiary = animateColor(targetValue = onTertiary),
    tertiary = animateColor(targetValue = tertiary),
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SquirrelTheme(content: @Composable () -> Unit) {
    val themeState = ThemeStateSingleton.themeState
    val snackbarHostState = remember { SnackbarHostState() }

    val colorScheme = (when (themeState.value) {
        ThemeState.AUTO -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        ThemeState.DARK -> DarkColorScheme
        ThemeState.LIGHT -> LightColorScheme
    }).switch()

    val statusBarTextColor = when (themeState.value) {
        ThemeState.DARK -> false
        ThemeState.LIGHT -> true
        ThemeState.AUTO -> !isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                statusBarTextColor

        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides CustomRippleTheme,
            LocalThemeState provides themeState,
            LocalSnackBarHostState provides snackbarHostState,
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                content = { content() }
            )
        }
    }
}

