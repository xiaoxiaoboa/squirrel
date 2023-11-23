package com.squirrel.data.repository.impl

import com.squirrel.data.dao.EntireDao
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.ItemEntity
import com.squirrel.data.repository.EntireRepository
import javax.inject.Inject

class EntireRepositoryImpl @Inject constructor(
    private val entireDao: EntireDao
) : EntireRepository {
    override suspend fun getItemCount(dayId: String): Int {
        return entireDao.getItemCount(dayId)
    }


    override suspend fun insertTwo(day: DayEntity, item: ItemEntity) {
        entireDao.insertTwo(day, item)
    }

    override suspend fun updateItemWithDay(item: ItemEntity, day: DayEntity) {
        entireDao.updateItemWithDay(item, day)
    }

    override suspend fun deleteItemWithToggleDay(item: ItemEntity, day: DayEntity) {
        entireDao.deleteItemWithToggleDay(item, day)
    }

    override suspend fun deleteItemWithDay(day: DayEntity) {
        entireDao.deleteItemWithDay(day)
    }
}