package com.squirrel.drive

import androidx.annotation.Keep
import com.squirrel.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

@Keep
class OkHttp {


    companion object {
        private val client = OkHttpClient()

        fun baseRequest(url: String): Response {
            try {

                val request = Request.Builder().url(url).build()

                return client.newCall(request).execute()
            } catch (e: Exception) {
                throw e
            }
        }


        fun basePutRequest(
            url: String,
            body: RequestBody = Constants.EMPTY_STRING.toRequestBody(),
            token: String = Constants.EMPTY_STRING,
            contentType: String = Constants.CONTENT_TYPE
        ): Response {
            try {

                val request = Request.Builder().url(url).put(body).header("Authorization", token)
                    .header("Content-Type", contentType).build()
                return client.newCall(request).execute()
            } catch (e: Exception) {
                throw e
            }
        }

        fun basePostRequest(
            url: String,
            body: RequestBody = Constants.EMPTY_STRING.toRequestBody(),
            token: String = Constants.EMPTY_STRING,
            contentType: String = Constants.CONTENT_TYPE
        ): Response {
            val request = Request.Builder().url(url).post(body).header("Authorization", token)
                .header("Content-Type", contentType).build()
            try {
                return client.newCall(request).execute()
            } catch (e: Exception) {
                throw e
            }
        }
    }


}