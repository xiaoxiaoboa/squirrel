package com.squirrel.drive.aliyun.api

import android.content.Context
import androidx.annotation.Keep
import com.squirrel.drive.aliyun.types.CreateFileResponse
import com.squirrel.drive.aliyun.types.GetDownloadUrlResponse
import com.squirrel.drive.aliyun.types.GetTokenResponse
import com.squirrel.drive.aliyun.types.GetUserResponse
import com.squirrel.drive.aliyun.types.PartInfoList
import com.squirrel.drive.aliyun.types.SearchFileResponse


interface AliApi {
    suspend fun getToken(authCode: String): GetTokenResponse?

    suspend fun getUser(token: String): GetUserResponse?

    suspend fun createDriveFolder(driveId: String, token: String): CreateFileResponse?

    suspend fun refreshToken(
        clientId: String, refreshToken: String, clientSecret: String
    ): GetTokenResponse?

    suspend fun createBackupFile(
        context: Context,
        driveId: String,
        parentFileId: String,
        token: String
    ): CreateFileResponse?

    suspend fun uploadBackupFileOfPart(
        context: Context,
        partInfo: PartInfoList,
        length: Int
    ): Float?

    suspend fun deletePreviousDriveFile(driveId: String, fileId: String, token: String)

    suspend fun uploadComplete(driveId: String, fileId: String, uploadId: String, token: String)

    suspend fun searchFile(
        driveId: String,
        parentFileId: String,
        fileName: String,
        fileType: String,
        token: String
    ): SearchFileResponse?

    suspend fun getFileDownloadUrl(
        driveId: String,
        fileId: String,
        token: String
    ): GetDownloadUrlResponse?

    suspend fun downloadFile(
        context: Context,
        url: String,
        setProcessValue: (value: Float) -> Unit
    )
}