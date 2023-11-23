package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class GetUserResponse(
    @Json(name = "user_id") val userId: String = Constants.EMPTY_STRING,
    val name: String = Constants.EMPTY_STRING,
    val avatar: String = Constants.EMPTY_STRING,
    val phone: String = Constants.EMPTY_STRING,
    @Json(name = "default_drive_id") val defaultDriveId: String = Constants.EMPTY_STRING,
    @Json(name = "resource_drive_id") val resourceDriveId: String = Constants.EMPTY_STRING,
    @Json(name = "backup_drive_id") val backupDriveId: String = Constants.EMPTY_STRING,
)
