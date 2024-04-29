package com.squirrel.utils

import android.icu.util.Calendar
import com.squirrel.utils.types.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/* 格式:年月日 */
fun formatDate(
    year: Int = CurrentDate.YEAR,
    month: Int = CurrentDate.MONTH,
    day: Int = CurrentDate.DAY,
    formatter: String,
    time: String? = null,
): String {
    val date = LocalDate.of(year, month, day)
    val dateFormatter = DateTimeFormatter.ofPattern(formatter)
    val formatStr = date.format(dateFormatter)

    return if (time != null) "$formatStr $time" else formatStr
}

/* 格式：12:00 */
fun formatDateForCurrentTime(millis: Long = System.currentTimeMillis(), formatter: String): String {
    val dateFormat = SimpleDateFormat(formatter, Locale.getDefault())
    return dateFormat.format(java.util.Date(millis))
}

/* 格式：星期三*/
fun getDayOfWeek(year: Int, month: Int, day: Int): String {
    val date = LocalDate.of(year, month, day)
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.SIMPLIFIED_CHINESE)
}


fun formatDateForYearMonthDay(millis: Long): Date {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return Date(
        year = year,
        month = month,
        day = day,
        timestamp = millis
    )
}

fun getMillisFromDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val dateTime = LocalDateTime.of(year, month, day, hour, minute)
    val offset = ZoneId.systemDefault().rules.getOffset(dateTime)
    return dateTime.toInstant(offset).toEpochMilli()
}