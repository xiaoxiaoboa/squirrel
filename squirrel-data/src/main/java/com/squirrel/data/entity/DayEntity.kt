package com.squirrel.data.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey(autoGenerate = false) val dayId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    @ColumnInfo(name = "totalAmount", defaultValue = "empty")
    val totalAmount: Double,
)
