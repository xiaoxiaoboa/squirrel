package com.squirrel.utils

import androidx.annotation.Keep
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Keep
private val mmkv: MMKV = MMKV.defaultMMKV()

@Keep
object StorageSingleton {
    val storage: MMKV
        get() = mmkv
}

@Keep
suspend inline fun <reified T> serialization(value: T): String =
    withContext(Dispatchers.IO) {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(T::class.java)

        return@withContext jsonAdapter.toJson(value)
    }

@Keep
suspend inline fun <reified T> deserialization(value: String, type: T): T =
    withContext(Dispatchers.IO) {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(T::class.java)

        return@withContext jsonAdapter.fromJson(value)!!
    }

