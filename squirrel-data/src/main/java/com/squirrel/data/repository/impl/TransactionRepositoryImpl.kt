package com.squirrel.data.repository.impl

import com.squirrel.data.dao.TransactionDao
import com.squirrel.data.entity.TransactionEntity
import com.squirrel.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionDao: TransactionDao) : TransactionRepository {

//    override suspend fun insertItem(item: ItemEntity): Long {
//        return itemDao.insert(item)
//    }

    override suspend fun updateItem(item: TransactionEntity): Int {
        return transactionDao.update(item)
    }

    override suspend fun deleteItem(item: TransactionEntity): Int {
        return transactionDao.delete(item)
    }

    override suspend fun getItemById(itemId: Int): TransactionEntity {
        return transactionDao.getItemById(itemId)
    }

    override suspend fun getItemAll(): List<TransactionEntity> {
        return transactionDao.getItemAll()
    }

    override suspend fun getItemsCount(): Flow<Int> {
        return transactionDao.getItemsCount()
    }

}