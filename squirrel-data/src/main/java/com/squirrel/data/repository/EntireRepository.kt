package com.squirrel.data.repository

import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.ItemEntity

interface EntireRepository {
    suspend fun getItemCount(dayId: String): Int


    suspend fun insertTwo(day: DayEntity, item: ItemEntity)

    suspend fun updateItemWithDay(item: ItemEntity, day: DayEntity)

    suspend fun deleteItemWithToggleDay(item: ItemEntity, day: DayEntity)

    suspend fun deleteItemWithDay(day: DayEntity)

}