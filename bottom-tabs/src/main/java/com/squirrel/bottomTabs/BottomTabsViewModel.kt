package com.squirrel.bottomTabs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomTabsViewModel : ViewModel() {
    var currentRoute by mutableStateOf("")

    var previousRoute by mutableStateOf("")

}