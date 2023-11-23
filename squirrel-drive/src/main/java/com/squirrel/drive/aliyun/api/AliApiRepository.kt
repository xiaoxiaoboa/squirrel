package com.squirrel.drive.aliyun.api

import android.content.Context
import androidx.annotation.Keep
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squirrel.drive.OkHttp
import com.squirrel.drive.aliyun.ConstantsForAliYun
import com.squirrel.drive.aliyun.getPartInfoListForCreateFile
import com.squirrel.drive.aliyun.types.BackFileJsonBody
import com.squirrel.drive.aliyun.types.CreateFileResponse
import com.squirrel.drive.aliyun.types.DriveFolderJsonBody
import com.squirrel.drive.aliyun.types.GetDownloadUrlJsonBody
import com.squirrel.drive.aliyun.types.GetDownloadUrlResponse
import com.squirrel.drive.aliyun.types.GetTokenResponse
import com.squirrel.drive.aliyun.types.GetUserResponse
import com.squirrel.drive.aliyun.types.PartInfoList
import com.squirrel.drive.aliyun.types.PreviousDriveFileJsonBody
import com.squirrel.drive.aliyun.types.RefreshTokenJsonBody
import com.squirrel.drive.aliyun.types.SearchFileJsonBody
import com.squirrel.drive.aliyun.types.SearchFileResponse
import com.squirrel.drive.aliyun.types.TokenJsonBody
import com.squirrel.drive.aliyun.types.UploadCompleteJsonBody
import com.squirrel.utils.Constants
import com.squirrel.utils.getLocalFileSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import okio.buffer
import okio.sink
import java.io.File
import java.io.RandomAccessFile
import javax.inject.Inject


class AliApiRepository @Inject constructor() : AliApi {
    private val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    override suspend fun getToken(authCode: String): GetTokenResponse? =
        withContext(Dispatchers.IO) {
            try {
                val jsonAdapter = moshi.adapter(GetTokenResponse::class.java)
                val jsonBodyAdapter = moshi.adapter(TokenJsonBody::class.java)
                val json = jsonBodyAdapter.toJson(
                    TokenJsonBody(
                        clientId = ConstantsForAliYun.APP_ID,
                        clientSecret = ConstantsForAliYun.APP_SECRET,
                        code = authCode
                    )
                )
                OkHttp.basePostRequest(
                    url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.GET_TOKEN}",
                    body = json.toRequestBody(),
                    contentType = Constants.CONTENT_TYPE
                ).use { res ->
                    if (res.isSuccessful) {
                        return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                    } else {
                        throw IOException("${res.body?.string()}")
                    }
                }

            } catch (e: Exception) {
                println("getToken:$e")
                return@withContext null
            }
        }

    override suspend fun getUser(token: String): GetUserResponse? = withContext(Dispatchers.IO) {
        try {
            val jsonAdapter = moshi.adapter(GetUserResponse::class.java)
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.GET_USERINFO}",
                token = token
            ).use { res ->
                return@withContext jsonAdapter.fromJson(res.body!!.source())!!
            }

        } catch (e: Exception) {
            println("getUser:$e")
            return@withContext null
        }
    }

    override suspend fun createDriveFolder(driveId: String, token: String): CreateFileResponse? =
        withContext(Dispatchers.IO) {
            try {
                val jsonAdapter = moshi.adapter(CreateFileResponse::class.java)
                val jsonBodyAdapter = moshi.adapter(DriveFolderJsonBody::class.java)
                val json = jsonBodyAdapter.toJson(
                    DriveFolderJsonBody(
                        driveId = driveId
                    )
                )
                OkHttp.basePostRequest(
                    url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.CREATE_FILE}",
                    body = json.toRequestBody(),
                    token = token
                ).use { res ->
                    if (res.isSuccessful) {
                        return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                    } else {
                        throw IOException(res.body?.string())
                    }
                }

            } catch (e: Exception) {
                println("createDriveFolder:$e")
                return@withContext null
            }
        }

    override suspend fun refreshToken(
        clientId: String, refreshToken: String, clientSecret: String
    ): GetTokenResponse? = withContext(Dispatchers.IO) {
        try {
            val jsonAdapter = moshi.adapter(GetTokenResponse::class.java)
            val jsonBodyAdapter = moshi.adapter(RefreshTokenJsonBody::class.java)
            val json = jsonBodyAdapter.toJson(
                RefreshTokenJsonBody(
                    clientId = clientId, clientSecret = clientSecret, refreshToken = refreshToken
                )
            )

            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.GET_TOKEN}",
                body = json.toRequestBody()
            ).use { res ->
                if (res.isSuccessful) {
                    return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                } else {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("refreshToken:$e")
            return@withContext null
        }
    }

    override suspend fun createBackupFile(
        context: Context, driveId: String, parentFileId: String, token: String
    ): CreateFileResponse? = withContext(Dispatchers.IO) {
        try {
            val fileSize = getLocalFileSize(context = context)
            val jsonAdapter = moshi.adapter(CreateFileResponse::class.java)
            val jsonBodyAdapter = moshi.adapter(BackFileJsonBody::class.java)
            val json = jsonBodyAdapter.toJson(
                BackFileJsonBody(
                    driveId = driveId,
                    parentFileId = parentFileId,
                    partInfoList = getPartInfoListForCreateFile(fileSize)
                )
            )
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.CREATE_FILE}",
                body = json.toRequestBody(),
                token = token
            ).use { res ->
                if (res.isSuccessful) {
                    return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                } else {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("createBackupFile:$e")
            return@withContext null
        }
    }

    override suspend fun uploadBackupFileOfPart(
        context: Context, partInfo: PartInfoList, length: Int
    ): Float? = withContext(Dispatchers.IO) {
        val fileSize = getLocalFileSize(context)
        val partSize = (fileSize / length)
        val number = partInfo.partNumber
        val pos = (number - 1) * partSize
        val size = (fileSize - pos).coerceAtMost(partSize)
        val buffer = ByteArray(size.toInt())

        val file = File(context.cacheDir, Constants.BACKUP_FILE_NAME)
        try {
            val randomAccessFile = RandomAccessFile(file, "r")
            randomAccessFile.seek(pos)
            randomAccessFile.readFully(buffer, 0, size.toInt())
            randomAccessFile.close()

            val body = buffer.toRequestBody()

            OkHttp.basePutRequest(
                url = partInfo.uploadUrl,
                contentType = "",
                body = body,
            ).use { res ->
                if (res.isSuccessful) {
                    return@withContext size.toFloat() / fileSize
                } else {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("uploadBackupFileOfPart:$e")
            return@withContext null
        }
    }

    override suspend fun deletePreviousDriveFile(
        driveId: String, fileId: String, token: String
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val jsonBodyAdapter = moshi.adapter(PreviousDriveFileJsonBody::class.java)
            val json = jsonBodyAdapter.toJson(
                PreviousDriveFileJsonBody(
                    driveId = driveId, fileId = fileId
                )
            )
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.DELETE_FILE}",
                body = json.toRequestBody(),
                token = token
            ).use { res ->
                if (!res.isSuccessful) {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("deletePreviousDriveFile:$e")
        }
    }

    override suspend fun uploadComplete(
        driveId: String, fileId: String, uploadId: String, token: String
    ): Unit = withContext(Dispatchers.IO) {
        try {
            val jsonAdapter = moshi.adapter(UploadCompleteJsonBody::class.java)
            val json = jsonAdapter.toJson(
                UploadCompleteJsonBody(
                    driveId = driveId, fileId = fileId, uploadId = uploadId
                )
            )
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.UPLOAD_COMPLETE}",
                body = json.toRequestBody(),
                token = token
            ).use { }
        } catch (e: Exception) {
            println("uploadComplete:$e")
        }
    }

    override suspend fun searchFile(
        driveId: String, parentFileId: String, fileName: String, fileType: String, token: String
    ): SearchFileResponse? = withContext(Dispatchers.IO) {
        try {
            val jsonAdapter = moshi.adapter(SearchFileResponse::class.java)
            val jsonBodyAdapter = moshi.adapter(SearchFileJsonBody::class.java)
            val json = jsonBodyAdapter.toJson(
                SearchFileJsonBody(
                    driveId = driveId,
                    query = "parent_file_id = ${"'"}$parentFileId${"'"} and type = ${"'"}$fileType${"'"} and name = ${"'"}${fileName}${"'"}"
                )
            )
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.SEARCH_FILE}",
                body = json.toRequestBody(),
                token = token
            ).use { res ->
                if (res.isSuccessful) {
                    return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                } else {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("searchFile:$e")
            return@withContext null
        }
    }

    override suspend fun getFileDownloadUrl(
        driveId: String, fileId: String, token: String
    ): GetDownloadUrlResponse? = withContext(Dispatchers.IO) {
        try {
            val jsonAdapter = moshi.adapter(GetDownloadUrlResponse::class.java)
            val jsonBodyAdapter = moshi.adapter(GetDownloadUrlJsonBody::class.java)
            val json = jsonBodyAdapter.toJson(
                GetDownloadUrlJsonBody(
                    driveId = driveId, fileId = fileId
                )
            )
            OkHttp.basePostRequest(
                url = "${ConstantsForAliYun.ALI_BASEURL}${ConstantsForAliYun.GET_DOWNLOAD_URL}",
                body = json.toRequestBody(),
                token = token
            ).use { res ->
                if (res.isSuccessful) {
                    return@withContext jsonAdapter.fromJson(res.body!!.source())!!
                } else {
                    throw IOException(res.body?.string())
                }
            }
        } catch (e: Exception) {
            println("getFileDownloadUrl:${e}")
            return@withContext null
        }
    }

    override suspend fun downloadFile(
        context: Context, url: String, setProcessValue: (value: Float) -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        val localFile = File(context.cacheDir, Constants.BACKUP_FILE_NAME)
        try {
            OkHttp.baseRequest(url = url).use { res ->
                if (res.isSuccessful) {
                    val source = res.body!!.source()
                    val length = res.body!!.contentLength()
                    val sink = localFile.sink().buffer()

                    var totalRead = 0L
                    var read: Long
                    while (source.read(sink.buffer, 1024L).also { read = it } != -1L) {
                        totalRead += read
                        sink.emitCompleteSegments()
                        setProcessValue((totalRead.toFloat() / length))
                    }
                    sink.writeAll(source)
                    sink.close()
                } else {
                    throw IOException(res.body?.string())
                }
            }

        } catch (e: Exception) {
            println("downloadFile:$e")
        }
    }
}


