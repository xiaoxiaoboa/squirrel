package com.squirrel.drive

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.squirrel.drive.aliyun.AliYunDrive
import com.squirrel.utils.Constants
import com.squirrel.utils.getNoMoreThanTwoDigits
import com.squirrel.utils.ui.SettingItemFrame
import com.squirrel.utils.ui.SettingItemHeader
import com.squirrel.utils.ui.SquirrelAlertDialog

@Composable
fun SyncDrive(
    navController: NavHostController,
    parentEntry: NavBackStackEntry,
    driveViewModel: DriveViewModel = hiltViewModel(parentEntry)
) {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SettingItemFrame(title = "账号") {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (driveViewModel.aliYunIsLogin) {
                    AfterLogin(driveName = "@阿里云盘",
                        avatar = driveViewModel.aliYunUserInfo.avatar,
                        nickName = driveViewModel.aliYunUserInfo.name,
                        enabled = !(driveViewModel.aliYunWaitNetWorkProcessing || driveViewModel.aliYunNetworkProcessing),
                        aliYunNetworkProcessing = driveViewModel.aliYunNetworkProcessing,
                        aliYunWaitNetWorkProcessing = driveViewModel.aliYunWaitNetWorkProcessing,
                        aliYunNetWorkProcessValue = driveViewModel.aliYunNetWorkProcessValue,
                        toggleLogoutAlertDialogShow = {
                            driveViewModel.toggleAliYunLogoutAlertDialogShow(true)
                        },
                        toggleAliYunBackupAlertDialogShow = {
                            driveViewModel.toggleAliYunBackupAlertDialogShow(true)
                        },
                        toggleAliYunRestoreAlertDialogShow = {
                            driveViewModel.toggleAliYunRestoreAlertDialogShow(true)
                        }

                    )
                } else {
                    Text(text = "还没有登录同步网盘~", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        SettingItemFrame(title = "同步") {
            AliYunDrive(navController = navController, enabled = !driveViewModel.aliYunIsLogin)
        }
    }

    if (driveViewModel.aliYunLogoutAlertDialogShow) {
        SquirrelAlertDialog(title = "退出账号",
            text = "确定要退出账号吗?",
            onDismissRequest = { driveViewModel.toggleAliYunLogoutAlertDialogShow(false) },
            onConfirmRequest = {
                driveViewModel.logoutAliYun()
                driveViewModel.toggleAliYunLogoutAlertDialogShow(false)
            })
    }
    if (driveViewModel.aliYunBackupAlertDialogShow) {
        SquirrelAlertDialog(title = "备份",
            text = stringResource(R.string.backup_tip),
            onDismissRequest = { driveViewModel.toggleAliYunBackupAlertDialogShow(false) },
            onConfirmRequest = {
                driveViewModel.createBackupFile(context = context)
                driveViewModel.toggleAliYunBackupAlertDialogShow(false)
            })
    }
    if (driveViewModel.aliYunRestoreAlertDialogShow) {
        SquirrelAlertDialog(title = "恢复",
            text = stringResource(R.string.restore_tip),
            onDismissRequest = { driveViewModel.toggleAliYunRestoreAlertDialogShow(false) },
            onConfirmRequest = {
                driveViewModel.downloadBackupFile(context = context)
                driveViewModel.toggleAliYunRestoreAlertDialogShow(false)
            })
    }
}

@Composable
fun DriveItem(
    enabled: Boolean,
    driveName: String,
    driveIcon: Int,
    desc: String = Constants.EMPTY_STRING,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }) {
        Row(Modifier.padding(vertical = 10.dp)) {
            SettingItemHeader(
                icon = driveIcon, title = driveName, description = desc
            )
        }
    }
}

@Composable
fun AfterLogin(
    avatar: String,
    nickName: String,
    driveName: String,
    aliYunNetworkProcessing: Boolean,
    aliYunWaitNetWorkProcessing: Boolean,
    enabled: Boolean,
    aliYunNetWorkProcessValue: Float,
    toggleLogoutAlertDialogShow: () -> Unit,
    toggleAliYunBackupAlertDialogShow: () -> Unit,
    toggleAliYunRestoreAlertDialogShow: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier
                    .padding(10.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(model = avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(65.dp)
                        .clickable(
                            enabled = enabled,
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            toggleLogoutAlertDialogShow()
                        })
                Column {
                    Text(
                        text = nickName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = driveName,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SyncButton(text = "备份", enabled = enabled) {
                    toggleAliYunBackupAlertDialogShow()
                }
                SyncButton(text = "恢复", enabled = enabled) {
                    toggleAliYunRestoreAlertDialogShow()
                }
            }
        }
        if (aliYunWaitNetWorkProcessing) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary
            )
        }
        if (aliYunNetworkProcessing) {
            Text(
                text = "${getNoMoreThanTwoDigits(aliYunNetWorkProcessValue * 100)}%",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = aliYunNetWorkProcessValue,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}


@Composable
fun SyncButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(18.dp))
            .clickable(enabled = enabled) { onClick() }) {
        Box(Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(text = text, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}