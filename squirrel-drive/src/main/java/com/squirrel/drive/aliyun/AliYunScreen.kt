package com.squirrel.drive.aliyun

import android.annotation.SuppressLint
import android.util.Base64
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.squirrel.drive.DriveViewModel
import com.squirrel.utils.Constants

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AliYunScreen(
    parentEntry: NavBackStackEntry,
    driveViewModel: DriveViewModel = hiltViewModel(parentEntry),
    base64Url: String
) {
    val url = String(Base64.decode(base64Url, Base64.DEFAULT))
    Box(
        Modifier
            .fillMaxSize()
    ) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val newUrl = request?.url.toString()
                        if (newUrl.contains(Constants.REDIRECT_YES) && !newUrl.contains(Constants.REDIRECT_NO)) {
                            val token = newUrl.split("=").last()
                            driveViewModel.getToken(token)
                        }
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }

                loadUrl(url)
            }
        })
    }
}