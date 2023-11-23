package com.squirrel.utils.types

import androidx.annotation.Keep
import com.squirrel.utils.Constants
import com.squirrel.utils.CurrentDate


data class Day(
    var dayId: String = "",
    val year: Int = CurrentDate.YEAR,
    val month: Int = CurrentDate.MONTH,
    val day: Int = CurrentDate.DAY,
    val dayExpenditure: Double = Constants.INIT_DOUBLE,
)
