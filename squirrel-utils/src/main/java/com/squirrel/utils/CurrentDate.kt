package com.squirrel.utils

import androidx.annotation.Keep
import java.time.LocalDate


object CurrentDate {
    val YEAR = LocalDate.now().year
    val MONTH = LocalDate.now().monthValue
    val DAY = LocalDate.now().dayOfMonth
}
