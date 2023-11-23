package com.squirrel.data.repository

import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.DayWithItem
import kotlinx.coroutines.flow.Flow

interface DayRepository {
//    suspend fun insertItem(day: DayEntity): Long

    suspend fun updateDay(day: DayEntity)

    suspend fun deleteDay(day: DayEntity)

    suspend fun getDayByMonth(year: Int, month: Int): Flow<List<DayWithItem>>

    suspend fun getDayByDate(year: Int, month: Int, day: Int): DayEntity?
}