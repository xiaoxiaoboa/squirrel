package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class TokenJsonBody(
    @Json(name = "client_id") val clientId: String = Constants.EMPTY_STRING,
    @Json(name = "client_secret") val clientSecret: String = Constants.EMPTY_STRING,
    @Json(name = "grant_type") val grantType: String = "authorization_code",
    val code: String = Constants.EMPTY_STRING,
)

@Keep
@JsonClass(generateAdapter = true)
data class DriveFolderJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "parent_file_id") val parentFileId: String = "root",
    val name: String = Constants.BACKUP_FOLDER_NAME,
    val type: String = "folder",
    @Json(name = "check_name_mode") val checkNameMode: String = "refuse",
)

@Keep
data class RefreshTokenJsonBody(
    @Json(name = "client_id") val clientId: String = Constants.EMPTY_STRING,
    @Json(name = "client_secret") val clientSecret: String = Constants.EMPTY_STRING,
    @Json(name = "grant_type") val grantType: String = "refresh_token",
    @Json(name = "refresh_token") val refreshToken: String = Constants.EMPTY_STRING,
)

@Keep
data class BackFileJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "parent_file_id") val parentFileId: String = Constants.EMPTY_STRING,
    val name: String = Constants.BACKUP_FILE_NAME,
    val type: String = "file",
    @Json(name = "check_name_mode") val checkNameMode: String = "ignore",
    @Json(name = "part_info_list") val partInfoList: List<UploadPartInfoList> = emptyList()
)

@Keep
data class PreviousDriveFileJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "file_id") val fileId: String = Constants.EMPTY_STRING,
)

@Keep
data class UploadCompleteJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "file_id") val fileId: String = Constants.EMPTY_STRING,
    @Json(name = "upload_id") val uploadId: String = Constants.EMPTY_STRING,
)

@Keep
data class SearchFileJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    val query: String = Constants.EMPTY_STRING
)

@Keep
data class GetDownloadUrlJsonBody(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "file_id") val fileId: String = Constants.EMPTY_STRING,
)