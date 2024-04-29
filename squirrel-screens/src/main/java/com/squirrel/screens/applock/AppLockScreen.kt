package com.squirrel.screens.applock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.squirrel.utils.ui.biometricModal
import com.squirrel.utils.ui.handleBiometricAuthResult
import com.squirrel.utils.R

@Composable
fun AppLockScreen(unLock: () -> Unit) {
    val context = LocalContext.current as FragmentActivity
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                biometricModal(
                    activity = context,
                    biometricPromptCallback = handleBiometricAuthResult(unLock)
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = ImageVector.vectorResource(R.drawable.lock_weight),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        LaunchedEffect(key1 = true) {
            biometricModal(
                activity = context,
                biometricPromptCallback = handleBiometricAuthResult(unLock)
            )
        }
    }

}