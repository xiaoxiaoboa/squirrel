package com.squirrel.utils.types

import androidx.annotation.DrawableRes
import androidx.annotation.Keep


data class Category(val id: String = "", val name: String = "", @DrawableRes val icon: Int = 0)
