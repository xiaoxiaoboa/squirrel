package com.squirrel.utils.types

import androidx.annotation.Keep
import com.squirrel.utils.Constants
import com.squirrel.utils.CurrentDate
import java.sql.Timestamp


data class Date(
    val year: Int = CurrentDate.YEAR,
    val month: Int = CurrentDate.MONTH,
    val day: Int = CurrentDate.DAY,
    val timestamp: Long = Constants.INIT_LONG


)
