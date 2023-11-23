package com.squirrel.drive.aliyun.types

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squirrel.utils.Constants

@Keep
@JsonClass(generateAdapter = true)
data class GetTokenResponse(
    @Json(name = "token_type") val tokenType: String = Constants.EMPTY_STRING,
    @Json(name = "access_token") val accessToken: String = Constants.EMPTY_STRING,
    @Json(name = "refresh_token") val refreshToken: String = Constants.EMPTY_STRING,
    @Json(name = "expires_in") val expiresIn: Int = 0,
    val timestamp: String = Constants.EMPTY_STRING
)
