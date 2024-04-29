package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.TransactionEntity

@Dao
interface CombineDao {
    @Upsert
    suspend fun upsertDay(day: DayEntity): Long

    @Upsert
    suspend fun upsertItem(item: TransactionEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteTransaction(item: TransactionEntity)

    @Delete
    suspend fun deleteDay(day: DayEntity)


    @Query("SELECT COUNT(*) FROM transactions WHERE transactions.dayId = :dayId")
    suspend fun getTransactionCount(dayId: String): Int

    @Transaction
    suspend fun insertCombine(day: DayEntity, item: TransactionEntity, account: AccountEntity) {
        upsertDay(day)
        upsertItem(item)
        updateAccount(account)
    }

    @Transaction
    suspend fun <T> updateCombine(
        item: TransactionEntity,
        day: T,
        account: T,
        delectableDay: DayEntity? = null
    ) {
        when (day) {
            is DayEntity -> {
                upsertDay(day)
            }

            is List<*> -> {
                day.map {
                    if (it is DayEntity) {
                        upsertDay(it)
                    }
                }
            }
        }
        delectableDay?.let {
            deleteDay(it)
        }
        when (account) {
            is AccountEntity -> {
                updateAccount(account)
            }

            is List<*> -> {
                account.map {
                    if (it is AccountEntity) {
                        updateAccount(it)
                    }
                }
            }
        }
        upsertItem(item)
    }

    @Transaction
    suspend fun deleteTransaction(item: TransactionEntity, day: DayEntity, account: AccountEntity) {
        deleteTransaction(item)
        upsertDay(day)
        updateAccount(account)
    }


    @Transaction
    suspend fun deleteDayWithTransaction(day: DayEntity, account: AccountEntity) {
        deleteDay(day)
        updateAccount(account)
    }
}