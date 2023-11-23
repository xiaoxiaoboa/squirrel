package com.squirrel.data.repository

import com.squirrel.data.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

//    suspend fun insertItem(item: ItemEntity): Long

    suspend fun updateItem(item: ItemEntity): Int

    suspend fun deleteItem(item: ItemEntity): Int

    suspend fun getItemById(itemId: Int): ItemEntity

    suspend fun getItemAll(): List<ItemEntity>

    suspend fun getItemsCount(): Flow<Int>

}