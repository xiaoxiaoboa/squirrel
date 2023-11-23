package com.squirrel.utils

import android.content.Context
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


suspend fun zip(context: Context) = withContext(Dispatchers.IO) {
    val zipFile = File(context.cacheDir, Constants.BACKUP_FILE_NAME).toUri()
    val files = listOf(
        File(context.getDatabasePath(Constants.DATABASE_NAME), ""),
        File(context.getDatabasePath(Constants.DATABASE_SHM), ""),
        File(context.getDatabasePath(Constants.DATABASE_WAL), "")
    )
    try {
        context.contentResolver.openFileDescriptor(zipFile, "w").use { descriptor ->
            descriptor?.fileDescriptor?.let {
                ZipOutputStream(BufferedOutputStream(FileOutputStream(it))).use { outStream ->
                    files.forEach { file ->
                        outStream.putNextEntry(ZipEntry(file.name))
                        BufferedInputStream(FileInputStream(file)).use { inStream ->
                            inStream.copyTo(outStream)
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        println(e)
    }

}

suspend fun unzip(context: Context) = withContext(Dispatchers.IO) {
    val zipFile = File(context.cacheDir, Constants.BACKUP_FILE_NAME).toUri()
    val targetDir = File(context.dataDir, "databases")

    try {
        context.contentResolver.openFileDescriptor(zipFile, "r").use { descriptor ->
            descriptor?.fileDescriptor?.let {
                ZipInputStream(BufferedInputStream(FileInputStream(it))).use { inStream ->
                    unzip(inStream, targetDir)
                }
            }
        }
    } catch (e: Exception) {
        println(e)
    }

}

private suspend fun unzip(inStream: ZipInputStream, targetDir: File) = withContext(Dispatchers.IO) {
    val locationPath = targetDir.absolutePath.let {
        if (!it.endsWith(File.separator)) "$it${File.separator}"
        else it
    }

    var zipEntry: ZipEntry?
    var unzipFile: File


    while (inStream.nextEntry.also { zipEntry = it } != null) {
        unzipFile = File(locationPath + zipEntry!!.name)

        BufferedOutputStream(FileOutputStream(unzipFile)).use { outStream ->
            inStream.copyTo(outStream)
        }

    }
}