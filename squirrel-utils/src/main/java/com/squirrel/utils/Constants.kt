package com.squirrel.utils

import androidx.annotation.Keep


object Constants {
    const val INIT_DOUBLE = 0.00
    const val INIT_FLOAT = 0.0f
    const val INIT_INT = 0
    const val INIT_LONG = 0L
    const val EMPTY_STRING = ""
    const val HOUR_MINUTE = "HH:mm"
    const val YEAR_MONTH_DAY = "yyyy年MM月dd日"
    const val MONTH_DAY = "MM-dd"
    const val ACCOUNT_VALUE = 1
    const val INIT_FALSE = false
    const val INIT_TRUE = true
    const val BACKUP_FILE_TIME_FORMAT = "yyyy-MM-dd HH_mm"
    const val REDIRECT_YES = "redirect?code"
    const val REDIRECT_NO = "redirect?error"
    const val CONTENT_TYPE = "application/json"
    const val BACKUP_FILE_NAME = "squirrel_backup.zip"
    const val BACKUP_FOLDER_NAME = "squirrel_backup"


    /* localStorage key*/
    const val ALIYUN_USER_INFO = "aliyun_user_info"
    const val THEME = "theme"
    const val ALIYUN_BACKUP_FOLDER = "aliyun_backup_folder"
    const val AliYun_BACKUP_FILE = "backup_file"
    const val TOKEN = "token"
    const val ALIYUN_IS_LOGIN = "aliyun_isLogin"
    const val APP_LOCK_ENABLE = "appLockEnable"

    /* database*/
    const val DATABASE_NAME = "squirrel.db"
    const val DATABASE_INIT_FILE = "database/squirrel_init.db"
    const val DATABASE_SHM = "squirrel.db-shm"
    const val DATABASE_WAL = "squirrel.db-wal"
}