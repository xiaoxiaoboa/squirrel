package com.squirrel.data.repository.impl

import com.squirrel.data.dao.CombineDao
import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.TransactionEntity
import com.squirrel.data.repository.CombineRepository
import javax.inject.Inject

class CombineRepositoryImpl @Inject constructor(
    private val combineDao: CombineDao
) : CombineRepository {
    override suspend fun getTransactionCount(dayId: String): Int {
        return combineDao.getTransactionCount(dayId)
    }

    override suspend fun deleteDay(day: DayEntity) {
        combineDao.deleteDay(day)
    }

    override suspend fun insertCombine(
        day: DayEntity,
        item: TransactionEntity,
        account: AccountEntity
    ) {
        combineDao.insertCombine(day, item, account)
    }

    override suspend fun <T> updateCombine(
        item: TransactionEntity,
        day: T,
        account: T,
        delectableDay: DayEntity?
    ) {
        combineDao.updateCombine(item, day, account, delectableDay)
    }

    override suspend fun deleteTransaction(
        item: TransactionEntity,
        day: DayEntity,
        account: AccountEntity
    ) {
        combineDao.deleteTransaction(item, day, account)
    }

    override suspend fun deleteDayWithTransaction(day: DayEntity, account: AccountEntity) {
        combineDao.deleteDayWithTransaction(day, account)
    }
}