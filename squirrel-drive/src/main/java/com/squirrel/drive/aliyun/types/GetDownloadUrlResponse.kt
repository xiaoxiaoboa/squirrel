package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class GetDownloadUrlResponse(
    val url: String = Constants.EMPTY_STRING,
    val expiration: String = Constants.EMPTY_STRING,
    val method: String = Constants.EMPTY_STRING,
)
