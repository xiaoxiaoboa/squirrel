package com.squirrel.drive

import android.content.Context
import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirrel.data.repository.ItemRepository
import com.squirrel.drive.aliyun.ConstantsForAliYun
import com.squirrel.drive.aliyun.api.AliApiRepository
import com.squirrel.drive.aliyun.types.CreateFileResponse
import com.squirrel.drive.aliyun.types.GetDownloadUrlResponse
import com.squirrel.drive.aliyun.types.GetTokenResponse
import com.squirrel.drive.aliyun.types.GetUserResponse
import com.squirrel.drive.aliyun.types.SearchFileResponse
import com.squirrel.utils.Constants
import com.squirrel.utils.StorageSingleton
import com.squirrel.utils.deserialization
import com.squirrel.utils.serialization
import com.squirrel.utils.unzip
import com.squirrel.utils.zip
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DriveViewModel @Inject constructor(
    private val aliApiRepository: AliApiRepository, private val itemRepository: ItemRepository
) : ViewModel() {
    private var aliYunToken by mutableStateOf(GetTokenResponse())
    var aliYunUserInfo by mutableStateOf(GetUserResponse())
        private set
    var aliYunLogoutAlertDialogShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var aliYunIsLogin by mutableStateOf(Constants.INIT_FALSE)
        private set
    private var aliYunBackupFolder by mutableStateOf(CreateFileResponse())
    private var aliYunBackupFile by mutableStateOf(CreateFileResponse())
    private var aliYunBackupFileCache by mutableStateOf(CreateFileResponse())
    var aliYunNetworkProcessing by mutableStateOf(Constants.INIT_FALSE)
        private set
    var aliYunNetWorkProcessValue by mutableFloatStateOf(Constants.INIT_FLOAT)
        private set
    var aliYunWaitNetWorkProcessing by mutableStateOf(Constants.INIT_FALSE)
        private set
    var aliYunBackupAlertDialogShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    var aliYunRestoreAlertDialogShow by mutableStateOf(Constants.INIT_FALSE)
        private set
    private var itemsCount by mutableIntStateOf(Constants.INIT_INT)

    init {
        getItemsCount()
        aliYunIsLogin = StorageSingleton.storage.decodeBool(Constants.ALIYUN_IS_LOGIN)
        viewModelScope.launch {
            StorageSingleton.storage.decodeString(Constants.ALIYUN_USER_INFO)?.let {
                aliYunUserInfo = deserialization(it, GetUserResponse())
            }
            StorageSingleton.storage.decodeString(Constants.TOKEN)?.let {
                aliYunToken = deserialization(it, GetTokenResponse())
            }
            StorageSingleton.storage.decodeString(Constants.ALIYUN_BACKUP_FOLDER)?.let {
                aliYunBackupFolder = deserialization(it, CreateFileResponse())
            }
            StorageSingleton.storage.decodeString(Constants.AliYun_BACKUP_FILE)?.let {
                val backupFileInfo = deserialization(it, CreateFileResponse())
                aliYunBackupFile = backupFileInfo
                aliYunBackupFileCache = backupFileInfo
            }
        }
    }

    private fun getItemsCount() {
        viewModelScope.launch {
            itemRepository.getItemsCount().collect() {
                itemsCount = it
            }
        }
    }

    fun getToken(authCode: String) {
        viewModelScope.launch {
            aliApiRepository.getToken(authCode)?.let {
                aliYunToken = it.copy(timestamp = System.currentTimeMillis().toString())
                StorageSingleton.storage.encode(Constants.TOKEN, serialization(aliYunToken))
                getUserInfo(token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}")
            }

        }
    }

    private fun getUserInfo(token: String) {
        viewModelScope.launch {
            aliApiRepository.getUser(token)?.let {
                aliYunUserInfo = it
                StorageSingleton.storage.encode(
                    Constants.ALIYUN_USER_INFO, serialization(aliYunUserInfo)
                )

                aliYunIsLogin = Constants.INIT_TRUE
                StorageSingleton.storage.encode(Constants.ALIYUN_IS_LOGIN, Constants.INIT_TRUE)

                createDriveFolder(
                    driveId = aliYunUserInfo.defaultDriveId,
                    token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
                )
            }
        }
    }

    private suspend fun createDriveFolder(driveId: String, token: String) {
        aliApiRepository.createDriveFolder(
            driveId = driveId, token = token
        )?.let {
            aliYunBackupFolder = it
            StorageSingleton.storage.encode(
                Constants.ALIYUN_BACKUP_FOLDER, serialization(aliYunBackupFolder)
            )
        }
    }

    private suspend fun searchDriveFile(
        parentFileId: String, fileName: String, fileType: String
    ): SearchFileResponse? {
        return aliApiRepository.searchFile(
            driveId = aliYunUserInfo.defaultDriveId,
            parentFileId = parentFileId,
            fileName = fileName,
            fileType = fileType,
            token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
        )
    }

    fun createBackupFile(context: Context) {
        if (itemsCount == 0) {/* TODO 提示用户*/
            return
        }
        aliYunWaitNetWorkProcessing = Constants.INIT_TRUE
        viewModelScope.launch {

            isRefreshToken()

            searchDriveFile(
                parentFileId = "root",
                fileType = "folder",
                fileName = Constants.BACKUP_FOLDER_NAME,
            )?.let { search ->
                if (search.items.isEmpty()) {
                    createDriveFolder(
                        driveId = aliYunUserInfo.defaultDriveId,
                        token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
                    )
                }
                if (aliYunBackupFolder.fileId.isEmpty()) {
                    val folder = search.items.first()
                    aliYunBackupFolder = CreateFileResponse(
                        driveId = folder.driveId, fileId = folder.fileId, fileName = folder.name
                    )
                }
            }

            if (aliYunBackupFileCache.fileId.isEmpty()) {
                searchDriveFile(
                    parentFileId = aliYunBackupFolder.fileId,
                    fileType = "file",
                    fileName = Constants.BACKUP_FILE_NAME,
                )?.let { search ->
                    search.items.map { file ->
                        aliApiRepository.deletePreviousDriveFile(
                            driveId = aliYunUserInfo.defaultDriveId,
                            fileId = file.fileId,
                            token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
                        )
                    }
                }
            }

            zip(context)

            aliApiRepository.createBackupFile(
                context = context,
                driveId = aliYunUserInfo.defaultDriveId,
                parentFileId = aliYunBackupFolder.fileId,
                token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
            )?.let { createdFile ->
                aliYunBackupFile = createdFile

                StorageSingleton.storage.encode(
                    Constants.AliYun_BACKUP_FILE, serialization(aliYunBackupFile)
                )

                delay(2000)
                aliYunWaitNetWorkProcessing = Constants.INIT_FALSE
                aliYunNetworkProcessing = Constants.INIT_TRUE

                aliYunBackupFile.partInfoList?.let { list ->
                    for (i in list) {
                        aliApiRepository.uploadBackupFileOfPart(
                            context = context, partInfo = i, length = list.size
                        )?.let { value ->
                            aliYunNetWorkProcessValue += value
                        }
                    }
                    aliYunBackupFile.uploadId?.let { id ->
                        aliApiRepository.uploadComplete(
                            driveId = aliYunUserInfo.defaultDriveId,
                            fileId = aliYunBackupFile.fileId,
                            uploadId = id,
                            token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
                        )

                        if (aliYunBackupFileCache.fileId.isNotEmpty()) {
                            aliApiRepository.deletePreviousDriveFile(
                                driveId = aliYunUserInfo.defaultDriveId,
                                fileId = aliYunBackupFileCache.fileId,
                                token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
                            )
                        }

                        aliYunBackupFileCache = createdFile

                        delay(10000)
                    }
                }
            }
            aliYunNetworkProcessing = Constants.INIT_FALSE
            aliYunNetWorkProcessValue = Constants.INIT_FLOAT
            aliYunWaitNetWorkProcessing = Constants.INIT_FALSE
        }
    }

    private suspend fun getBackupFileDownloadUrl(): GetDownloadUrlResponse? {
        return aliApiRepository.getFileDownloadUrl(
            driveId = aliYunUserInfo.defaultDriveId,
            fileId = aliYunBackupFile.fileId,
            token = "${aliYunToken.tokenType} ${aliYunToken.accessToken}"
        )
    }

    fun downloadBackupFile(context: Context) {
        aliYunWaitNetWorkProcessing = Constants.INIT_TRUE
        viewModelScope.launch {

            isRefreshToken()

            var urlRes = getBackupFileDownloadUrl()

            if (urlRes === null) {
                searchDriveFile(
                    parentFileId = aliYunBackupFolder.fileId,
                    fileType = "file",
                    fileName = Constants.BACKUP_FILE_NAME,
                )?.let { search ->
                    if (search.items.isNotEmpty()) {
                        val file = search.items.first()
                        aliYunBackupFile = CreateFileResponse(
                            driveId = file.driveId, fileId = file.fileId, fileName = file.name
                        )
                        urlRes = getBackupFileDownloadUrl()
                    }
                }
            }
            delay(2000)
            if (urlRes !== null) {
                aliYunWaitNetWorkProcessing = Constants.INIT_FALSE
                aliYunNetworkProcessing = Constants.INIT_TRUE

                aliApiRepository.downloadFile(context = context, url = urlRes!!.url) {
                    aliYunNetWorkProcessValue = it
                }

                unzip(context)
                delay(10000)
            } else {/* TODO 提示用户云端没有文件*/
                println("云端没有文件")
            }


            aliYunWaitNetWorkProcessing = Constants.INIT_FALSE
            aliYunNetworkProcessing = Constants.INIT_FALSE
            aliYunNetWorkProcessValue = Constants.INIT_FLOAT
        }
    }

    private suspend fun isRefreshToken() {
        if (isTokenExpire()) {
            aliApiRepository.refreshToken(
                clientId = ConstantsForAliYun.APP_ID,
                clientSecret = ConstantsForAliYun.APP_SECRET,
                refreshToken = aliYunToken.refreshToken
            )?.let {
                aliYunToken = it.copy(timestamp = System.currentTimeMillis().toString())
            }
        }
    }

    fun logoutAliYun() {
        aliYunToken = GetTokenResponse()
        aliYunUserInfo = GetUserResponse()
        aliYunBackupFile = CreateFileResponse()
        aliYunBackupFolder = CreateFileResponse()
        aliYunIsLogin = Constants.INIT_FALSE
        viewModelScope.launch {
            StorageSingleton.storage.encode(
                Constants.ALIYUN_USER_INFO, serialization(GetUserResponse())
            )
            StorageSingleton.storage.encode(
                Constants.ALIYUN_BACKUP_FOLDER, serialization(GetTokenResponse())
            )
            StorageSingleton.storage.encode(
                Constants.AliYun_BACKUP_FILE, serialization(GetTokenResponse())
            )
            StorageSingleton.storage.encode(Constants.TOKEN, serialization(GetTokenResponse()))
            StorageSingleton.storage.encode(Constants.ALIYUN_IS_LOGIN, Constants.INIT_FALSE)
        }
    }

    fun toggleAliYunLogoutAlertDialogShow(show: Boolean) {
        aliYunLogoutAlertDialogShow = show
    }

    fun toggleAliYunBackupAlertDialogShow(show: Boolean) {
        aliYunBackupAlertDialogShow = show
    }

    fun toggleAliYunRestoreAlertDialogShow(show: Boolean) {
        aliYunRestoreAlertDialogShow = show
    }

    private fun isTokenExpire(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val tokenTimestamp = aliYunToken.timestamp.toLong() / 1000
        return (currentTime - tokenTimestamp) > aliYunToken.expiresIn
    }
}