package com.squirrel.setting


import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirrel.data.SquirrelRoomDatabase
import com.squirrel.data.entity.toCSV
import com.squirrel.data.entity.toItem
import com.squirrel.data.repository.ItemRepository
import com.squirrel.utils.Accounts
import com.squirrel.utils.Constants
import com.squirrel.utils.StorageSingleton
import com.squirrel.utils.theme.ThemeState
import com.squirrel.utils.types.Item
import com.squirrel.utils.unzip
import com.squirrel.utils.zip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    private var items by mutableStateOf<List<Item>>(emptyList())
    var alertDialogShow by mutableStateOf(Constants.INIT_FALSE)


    fun toggleAlertDialogShow(value: Boolean) {
        alertDialogShow = value
    }


    private suspend fun getItems() {
        items = itemRepository.getItemAll().map { it.toItem() }
    }

    fun backupDbFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            zip(context)
            val sourceFile = File(context.cacheDir, Constants.BACKUP_FILE_NAME)
            val outputStream = contentResolver.openOutputStream(uri)

            outputStream?.use { output ->
                FileInputStream(sourceFile).use { input ->
                    input.copyTo(output)
                }
            }
        }
    }

    private suspend fun generateCSV(): String {
        getItems()
        return "年,月,日,时间,类别,花费,备注,支出账户\n${
            items.joinToString("\n") {
                it.toCSV(
                    Accounts
                )
            }
        }"
    }

    fun exportCSV(context: Context, uri: Uri) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            viewModelScope.launch() {
                val rows = generateCSV()
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { output ->
                        output.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                        val writer = output.writer(charset = Charsets.UTF_8)
                        writer.write(rows)
                        writer.close()
                    }

                }
            }
        }
    }

    fun restoreDbFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            SquirrelRoomDatabase.closeDatabase()
            val inputStream = context.contentResolver.openInputStream(uri)
            val targetFile = File(context.cacheDir, Constants.BACKUP_FILE_NAME)

            inputStream?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            unzip(context)
        }
    }


    fun toggleTheme(themeState: MutableState<ThemeState>, targetTheme: ThemeState) {
        themeState.value = targetTheme
        val themeString = when (targetTheme) {
            ThemeState.AUTO -> "auto"
            ThemeState.DARK -> "dark"
            ThemeState.LIGHT -> "light"
        }
        StorageSingleton.storage.encode(Constants.THEME, themeString)
    }

    fun toggleSnackBarState(snackBarHostState: SnackbarHostState) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message = "hello", duration = SnackbarDuration.Short)
        }
    }
}

