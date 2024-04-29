package com.squirrel.utils.types

import androidx.annotation.Keep


data class Account(
    val id: Int,
    val name: String,
    var totalAmount: Double
)

