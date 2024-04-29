package com.squirrel.screens.setting

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.squirrel.drive.SyncDrive
import com.squirrel.utils.Constants
import com.squirrel.utils.NavRoutes
import com.squirrel.utils.R
import com.squirrel.utils.formatDateForCurrentTime
import com.squirrel.utils.theme.ThemeState
import com.squirrel.utils.theme.ThemeStateSingleton
import com.squirrel.utils.ui.NavigationBottomBar
import com.squirrel.utils.ui.SettingItemFrame
import com.squirrel.utils.ui.SettingItemHeader
import com.squirrel.utils.ui.SquirrelAlertDialog
import com.squirrel.utils.ui.biometricModal
import com.squirrel.utils.ui.checkDeviceBiometric
import com.squirrel.utils.ui.handleBiometricAuthResult

@Composable
fun SettingScreen(
    navController: NavHostController,
    parentEntry: NavBackStackEntry,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {

    Scaffold(modifier = Modifier.padding(top = 15.dp), bottomBar = {
        NavigationBottomBar(currentRoute = NavRoutes.SETTING_ROUTE) {
            navController.navigate(route = it) {
                popUpTo(route = NavRoutes.SETTING_ROUTE) {
                    inclusive = true
                }
            }
        }
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                SyncDrive(navController = navController, parentEntry = parentEntry)

                Preference(settingViewModel::toggleTheme)

                ExportWithImport(
                    alertDialogShow = settingViewModel.alertDialogShow,
                    toggleAlertDialog = settingViewModel::toggleAlertDialogShow,
                    restoreDbFile = settingViewModel::restoreDbFile,
                    backupDbFile = settingViewModel::backupDbFile,
                    exportCSV = settingViewModel::exportCSV
                )

                AppSecurity(
                    appLockEnable = settingViewModel.appLockEnable,
                    toggleAppLockEnable = settingViewModel::toggleAppLockEnable,
                )
            }
        }
    }
}

@Composable
fun Preference(toggleTheme: (themeState: MutableState<ThemeState>, targetTheme: ThemeState) -> Unit) {
    val theme = ThemeStateSingleton.themeState

    SettingItemFrame(title = "偏好") {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SettingItemHeader(icon = R.drawable.theme, title = "主题", description = "默认跟随系统")
            Row(
                Modifier.padding(end = 10.dp),
            ) {
                ToggleThemeButton(
                    text = "自动", currentTheme = theme.value, targetTheme = ThemeState.AUTO
                ) { toggleTheme(theme, ThemeState.AUTO) }
                ToggleThemeButton(
                    text = "深色", currentTheme = theme.value, targetTheme = ThemeState.DARK
                ) { toggleTheme(theme, ThemeState.DARK) }
                ToggleThemeButton(
                    text = "浅色", currentTheme = theme.value, targetTheme = ThemeState.LIGHT
                ) { toggleTheme(theme, ThemeState.LIGHT) }
            }
        }
    }
}

@Composable
fun ExportWithImport(
    alertDialogShow: Boolean,
    toggleAlertDialog: (value: Boolean) -> Unit,
    restoreDbFile: (context: Context, uri: Uri) -> Unit,
    backupDbFile: (context: Context, uri: Uri) -> Unit,
    exportCSV: (context: Context, uri: Uri) -> Unit
) {
    val context = LocalContext.current
    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            it?.let {
                toggleAlertDialog(false)
                restoreDbFile(context, it)
            }
        }

    val exportCSVFileLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("text/csv")) {
            it?.let {
                exportCSV(context, it)
            }
        }
    val exportDataFileLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("application/zip")) {
            it?.let {
                backupDbFile(context, it)
            }
        }

    SettingItemFrame(title = "数据") {
        Column(Modifier.fillMaxWidth()) {
            ExportWithImportWrapper(
                icon = R.drawable.excel, title = "导出Excel", description = "将数据导出为Excel表格"
            ) {
                exportCSVFileLauncher.launch("Squirrel(${formatDateForCurrentTime(formatter = Constants.BACKUP_FILE_TIME_FORMAT)}).csv")
            }
            Divider(
                Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.End)
            )
            ExportWithImportWrapper(
                icon = R.drawable.backup, title = "备份", description = "将数据保存至手机"
            ) {
                exportDataFileLauncher.launch("Squirrel(${formatDateForCurrentTime(formatter = Constants.BACKUP_FILE_TIME_FORMAT)}).zip")
            }
            Divider(
                Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.End)
            )
            ExportWithImportWrapper(
                icon = R.drawable.import_data, title = "导入", description = "将已有的数据文件恢复"
            ) { toggleAlertDialog(true) }
        }
    }

    if (alertDialogShow) {
        SquirrelAlertDialog(title = "导入说明",
            text = "请导入本程序导出的.zip文件\n导入会覆盖现在程序中的支出数据\n导入后请重启程序",
            onDismissRequest = { toggleAlertDialog(false) },
            onConfirmRequest = {
                toggleAlertDialog(false)
                filePickerLauncher.launch("application/zip")
            })
    }
}

@Composable
fun ExportWithImportWrapper(icon: Int, title: String, description: String, onClick: () -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onClick() }) {
        Row(Modifier.padding(vertical = 10.dp)) {
            SettingItemHeader(
                icon = icon, title = title, description = description
            )
        }
    }
}


@Composable
fun ToggleThemeButton(
    text: String, currentTheme: ThemeState, targetTheme: ThemeState, onClick: () -> Unit
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(if (currentTheme == targetTheme) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.background)
            .clickable { onClick() }) {
        Box(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (currentTheme == targetTheme)  Color.White else MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun AppSecurity(
    appLockEnable: Boolean,
    toggleAppLockEnable: () -> Unit,
) {
    val context = LocalContext.current as FragmentActivity
    SettingItemFrame(title = "安全") {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SettingItemHeader(
                icon = R.drawable.lock, title = "加锁", description = "每次打开时都需验证身份"
            )
            Switch(checked = appLockEnable, thumbContent = {
                if (appLockEnable) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.check),
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
//                        tint = Color.White
                    )
                }
            }, colors = SwitchDefaults.colors(
                checkedBorderColor = MaterialTheme.colorScheme.onTertiary,
                checkedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                checkedThumbColor = MaterialTheme.colorScheme.onTertiary,
                checkedIconColor = Color.White
            ), onCheckedChange = {
                if (it) {
                    checkDeviceBiometric(activity = context) {
                        toggleAppLockEnable()
                    }
                } else {
                    biometricModal(
                        activity = context,
                        biometricPromptCallback = handleBiometricAuthResult(toggleAppLockEnable)
                    )
                }
            })
        }
    }
}