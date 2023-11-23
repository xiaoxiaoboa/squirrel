package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.ItemEntity

@Dao
interface EntireDao {
    @Upsert
    suspend fun upsertDay(day: DayEntity): Long

    @Upsert
    suspend fun upsertItem(item: ItemEntity)


    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Delete
    suspend fun deleteDay(day: DayEntity)

    @Query("SELECT COUNT(*) FROM ITEMS WHERE items.dayId = :dayId")
    suspend fun getItemCount(dayId: String): Int

    @Transaction
    suspend fun insertTwo(day: DayEntity, item: ItemEntity) {
        upsertDay(day)
        upsertItem(item)
    }

    @Transaction
    suspend fun updateItemWithDay(item: ItemEntity, day: DayEntity) {
        upsertDay(day)
        upsertItem(item)
    }

    @Transaction
    suspend fun deleteItemWithToggleDay(item: ItemEntity, day: DayEntity) {
        deleteItem(item)
        upsertDay(day)
    }

    @Transaction
    suspend fun deleteItemWithDay(day: DayEntity) {
        deleteDay(day)
    }
}