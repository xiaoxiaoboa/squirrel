package com.squirrel.data.repository

import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.TransactionEntity

interface CombineRepository {
    suspend fun getTransactionCount(dayId: String): Int

    suspend fun deleteDay(day: DayEntity)

    suspend fun insertCombine(day: DayEntity, item: TransactionEntity, account: AccountEntity)

    suspend fun <T> updateCombine(
        item: TransactionEntity,
        day: T,
        account: T,
        delectableDay: DayEntity? = null
    )

    suspend fun deleteTransaction(item: TransactionEntity, day: DayEntity, account: AccountEntity)

    suspend fun deleteDayWithTransaction(day: DayEntity, account: AccountEntity)

}