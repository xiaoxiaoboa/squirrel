package com.squirrel.data.repository.impl

import com.squirrel.data.dao.DayDao
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.DayWithItem
import com.squirrel.data.repository.DayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DayRepositoryImpl @Inject constructor(
    private val dayDao: DayDao,
) : DayRepository {
//    override suspend fun insertItem(day: DayEntity): Long {
//        return dayDao.insert(day)
//    }

    override suspend fun updateDay(day: DayEntity) {
        dayDao.update(day)
    }

    override suspend fun deleteDay(day: DayEntity) {
        dayDao.delete(day)
    }

    override suspend fun getDayByMonth(year: Int, month: Int): Flow<List<DayWithItem>> {
        return dayDao.getDayByMonth(year = year, month = month)
    }

    override suspend fun getDayByDate(year: Int, month: Int, day: Int): DayEntity? {
        return dayDao.getDayByDate(year = year, month = month, day = day)
    }
}