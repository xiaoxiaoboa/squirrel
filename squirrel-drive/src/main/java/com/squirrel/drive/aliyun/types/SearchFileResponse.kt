package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class SearchFileResponse(
    val items: List<SearchFileItemResponse> = emptyList(),
    @Json(name = "next_mark") val nextMark: String = Constants.EMPTY_STRING,
)

@Keep
@JsonClass(generateAdapter = true)
data class SearchFileItemResponse(
    @Json(name = "drive_id") val driveId: String = Constants.EMPTY_STRING,
    @Json(name = "file_id") val fileId: String = Constants.EMPTY_STRING,
    val name: String = Constants.EMPTY_STRING,
    val type: String = Constants.EMPTY_STRING,
)
