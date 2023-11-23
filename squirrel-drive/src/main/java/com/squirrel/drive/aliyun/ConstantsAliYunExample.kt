package com.squirrel.drive.aliyun

object ConstantsAliYunExample {
    // 注册阿里云开发者
    const val APP_ID = ""
    const val ALI_BASEURL = ""
    const val APP_SECRET = ""

    // 授权页面重定向链接
    const val AUTH_REDIRECT_URL = "" //encode

    /* 阿里云盘 api*/
    const val OAUTH = "/oauth/authorize"
    const val GET_TOKEN = "/oauth/access_token"
    const val GET_USERINFO = "/adrive/v1.0/user/getDriveInfo"
    const val SEARCH_FILE = "/adrive/v1.0/openFile/search"
    const val CREATE_FILE = "/adrive/v1.0/openFile/create"
    const val UPLOAD_COMPLETE = "/adrive/v1.0/openFile/complete"
    const val GET_DOWNLOAD_URL = "/adrive/v1.0/openFile/getDownloadUrl"
    const val DELETE_FILE = "/adrive/v1.0/openFile/delete"
    const val AUTH_URL =
        "${ALI_BASEURL}${OAUTH}?client_id=${APP_ID}&redirect_uri=${AUTH_REDIRECT_URL}&scope=user:" +
                "base,file:all:read,file:all:write&response_type=code"
}