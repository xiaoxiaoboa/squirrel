package com.squirrel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.ViewModel
import com.squirrel.utils.Constants
import com.squirrel.utils.StorageSingleton

class RootViewModel : ViewModel() {
    private var appLockEnable by mutableStateOf(Constants.INIT_FALSE)
    var appLocked by mutableStateOf<Boolean?>(null)
        private set

    init {
        StorageSingleton.storage.decodeBool("appLockEnable").let {
            appLockEnable = it
            appLocked = appLockEnable
        }
    }

    fun unLockApp() {
        appLocked = false
    }
}