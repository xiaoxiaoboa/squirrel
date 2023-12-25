package com.squirrel.drive.aliyun

import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.squirrel.drive.DriveItem
import com.squirrel.drive.R
import com.squirrel.utils.NavRoutes

@Composable
fun AliYunDrive(navController: NavHostController, enabled: Boolean) {
    DriveItem(
        driveIcon = R.drawable.aliyundrive,
        driveName = "阿里云盘",
        desc = "点击登录，将数据同步至阿里云盘",
        enabled = enabled
    ) {
        val base64Url =
            Base64.encodeToString(ConstantsForAliYun.AUTH_URL.toByteArray(), Base64.DEFAULT)
        navController.navigate("${NavRoutes.aliyunWebView}/$base64Url")
    }
}