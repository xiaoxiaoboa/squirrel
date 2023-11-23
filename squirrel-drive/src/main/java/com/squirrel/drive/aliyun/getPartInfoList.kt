package com.squirrel.drive.aliyun

import com.squirrel.drive.aliyun.types.UploadPartInfoList
import kotlin.math.ceil

fun getPartInfoListForCreateFile(fileSize: Long): List<UploadPartInfoList> {
    val byte = 1 * 1024 * 1024
    val partInfoList = mutableListOf<UploadPartInfoList>()
    when {
        fileSize < byte * 10 -> {
            partInfoList.add(UploadPartInfoList(1))
        }

        fileSize < byte * 50 -> {
            val chunkSize = 5.0 * byte
            getPartList(chunkSize, fileSize, partInfoList)
        }

        fileSize < byte * 100 -> {
            val chunkSize = 10.0 * byte
            getPartList(chunkSize, fileSize, partInfoList)
        }

        fileSize < byte * 200 -> {
            val chunkSize = 20.0 * byte
            getPartList(chunkSize, fileSize, partInfoList)
        }

        fileSize < byte * 500 -> {
            val chunkSize = 50.0 * byte
            getPartList(chunkSize, fileSize, partInfoList)
        }

        else -> {
            val chunkSize = 100.0 * byte
            getPartList(chunkSize, fileSize, partInfoList)
        }
    }
    return partInfoList.toList()
}

fun getPartList(
    chunkSize: Double, fileSize: Long, partInfoList: MutableList<UploadPartInfoList>
) {
    val chunkCount = ceil(fileSize / chunkSize).toInt()
    for (i in 1..chunkCount) {
        partInfoList.add(UploadPartInfoList(i))
    }
}
