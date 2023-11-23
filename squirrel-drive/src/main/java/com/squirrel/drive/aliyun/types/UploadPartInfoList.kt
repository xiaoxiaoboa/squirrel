package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UploadPartInfoList(
    @Json(name = "part_number") val partNumber: Int
)
