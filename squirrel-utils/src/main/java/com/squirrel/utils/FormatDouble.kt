package com.squirrel.utils

import java.text.DecimalFormat

fun formatDouble(number: Double): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(number)
}

fun formatMoneyDisplay(money: String): Int {
    return when {
        money.length <= 12 -> 44
        money.length == 13 -> 40
        money.length == 14 -> 38
        money.length == 16 -> 34
        money.length == 17 -> 32
        else -> 0
    }
}