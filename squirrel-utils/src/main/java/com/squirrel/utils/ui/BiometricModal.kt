package com.squirrel.utils.ui

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.jvm.internal.impl.util.CheckResult.SuccessCheck


fun biometricModal(
    biometricPromptCallback: BiometricPrompt.AuthenticationCallback, activity: FragmentActivity
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(
        activity, executor, biometricPromptCallback
    )

    val promptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle("必需的身份验证")
            .setSubtitle("请使用指纹或屏幕密码来验证你的身份")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ).setConfirmationRequired(false).build()

    biometricPrompt.authenticate(promptInfo)
}

fun checkDeviceBiometric(activity: FragmentActivity, successCheck: () -> Unit) {
    val biometricManager = BiometricManager.from(activity)
    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            successCheck()
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            println("此设备不支持")
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            println("此设备未启用加密验证")
        }
    }
}

fun handleBiometricAuthResult(successCallBack: () -> Unit): BiometricPrompt.AuthenticationCallback {
    return object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            successCallBack()
        }

        override fun onAuthenticationFailed() {
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        }
    }
}