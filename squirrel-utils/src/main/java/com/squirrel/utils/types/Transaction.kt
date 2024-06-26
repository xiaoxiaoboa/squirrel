package com.squirrel.utils.types

import com.squirrel.utils.Constants
import com.squirrel.utils.CurrentDate


data class Transaction(
    var itemId: String = Constants.EMPTY_STRING,
    val dayId: String = Constants.EMPTY_STRING,
    val year: Int = CurrentDate.YEAR,
    val month: Int = CurrentDate.MONTH,
    val day: Int = CurrentDate.DAY,
    val time: String = Constants.EMPTY_STRING,
    val remark: String = Constants.EMPTY_STRING,
    val amount: Double = Constants.INIT_DOUBLE,
    val account: Int = Constants.ACCOUNT_VALUE,
    val category: Int = Constants.INIT_INT,
    val timestamp: Long = Constants.INIT_LONG
)
