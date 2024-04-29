package com.squirrel.utils.types


data class DaysWithItems(
    val day: Day,
    val items: List<Transaction>
)
