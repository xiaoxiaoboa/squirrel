package com.squirrel.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalSnackBarHostState =
    compositionLocalOf<SnackbarHostState> { error("No SnackbarHostState provided") }

object SnackBarHostStateSingleton {
    val snackBarHostState: SnackbarHostState
        @Composable get() = LocalSnackBarHostState.current
}