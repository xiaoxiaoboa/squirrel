package com.squirrel.utils

import android.content.Context
import java.io.File

fun getLocalFileSize(context: Context): Long {
    val file = File(context.cacheDir, Constants.BACKUP_FILE_NAME)
    return file.length()
}

