package com.squirrel.utils

import java.math.RoundingMode
import java.text.DecimalFormat

fun getNoMoreThanTwoDigits(number: Float): Float {
    val format = DecimalFormat("0.0")
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(number).toFloat()
}