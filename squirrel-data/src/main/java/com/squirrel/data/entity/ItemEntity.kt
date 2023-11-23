package com.squirrel.data.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Keep
@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["dayId"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("dayId"),
    ],
)
data class ItemEntity(
    val dayId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val time: String,
    val remark: String,
    val exp: Double,
    val account: Int,
    val category: Int,
    val timestamp: Long,

    @PrimaryKey(autoGenerate = false)
    val itemId: String,
)
