package com.squirrel.utils.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun toggleTheme(theme: ThemeState): Boolean {

    return when (theme) {
        ThemeState.AUTO -> isSystemInDarkTheme()

        ThemeState.LIGHT -> false

        ThemeState.DARK -> true
    }

}