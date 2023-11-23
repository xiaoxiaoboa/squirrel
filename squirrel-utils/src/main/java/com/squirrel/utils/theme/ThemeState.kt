package com.squirrel.utils.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.squirrel.utils.Constants
import com.squirrel.utils.StorageSingleton


fun getLocalTheme(): ThemeState {
    val localTheme = StorageSingleton.storage.decodeString(Constants.THEME)

    return when (localTheme) {
        "auto" -> ThemeState.AUTO
        "dark" -> ThemeState.DARK
        "light" -> ThemeState.LIGHT
        else -> ThemeState.AUTO
    }
}

val LocalThemeState = staticCompositionLocalOf { mutableStateOf(getLocalTheme()) }

object ThemeStateSingleton {
    val themeState: MutableState<ThemeState>
        @Composable
        get() = LocalThemeState.current
}


enum class ThemeState {
    AUTO, LIGHT, DARK
}