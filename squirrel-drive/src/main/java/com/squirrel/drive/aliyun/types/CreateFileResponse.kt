package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class CreateFileResponse(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "file_id") val fileId: String = Constants.EMPTY_STRING,
    @Json(name = "file_name") val fileName: String = Constants.EMPTY_STRING,
    @Json(name = "upload_id") val uploadId: String? = Constants.EMPTY_STRING,
    @Json(name = "part_info_list") val partInfoList: List<PartInfoList>? = emptyList(),
    val exist: Boolean? = Constants.INIT_FALSE
)

@Keep
@JsonClass(generateAdapter = true)
data class PartInfoList(
    @Json(name = "part_number") val partNumber: Int = Constants.INIT_INT,
    @Json(name = "upload_url") val uploadUrl: String = Constants.EMPTY_STRING,
    @Json(name = "part_size") val partSize: Int? = Constants.INIT_INT,
)