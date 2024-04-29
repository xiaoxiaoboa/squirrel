package com.squirrel.data.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Keep
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["dayId"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["account"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("dayId"),
        Index("account")
    ],
)
data class TransactionEntity(
    val dayId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val time: String,
    val remark: String,
    @ColumnInfo(name = "amount", defaultValue = "0.0")
    val amount: Double,
    val account: Int,
    val category: Int,
    val timestamp: Long,

    @PrimaryKey(autoGenerate = false)
    val itemId: String,
)
