package com.squirrel.data.repository

import com.squirrel.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

//    suspend fun insertItem(item: ItemEntity): Long

    suspend fun updateItem(item: TransactionEntity): Int

    suspend fun deleteItem(item: TransactionEntity): Int

    suspend fun getItemById(itemId: Int): TransactionEntity

    suspend fun getItemAll(): List<TransactionEntity>

    suspend fun getItemsCount(): Flow<Int>

}