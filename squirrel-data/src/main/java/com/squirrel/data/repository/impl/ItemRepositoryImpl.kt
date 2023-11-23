package com.squirrel.data.repository.impl

import com.squirrel.data.dao.ItemDao
import com.squirrel.data.entity.ItemEntity
import com.squirrel.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(private val itemDao: ItemDao) : ItemRepository {

//    override suspend fun insertItem(item: ItemEntity): Long {
//        return itemDao.insert(item)
//    }

    override suspend fun updateItem(item: ItemEntity): Int {
        return itemDao.update(item)
    }

    override suspend fun deleteItem(item: ItemEntity): Int {
        return itemDao.delete(item)
    }

    override suspend fun getItemById(itemId: Int): ItemEntity {
        return itemDao.getItemById(itemId)
    }

    override suspend fun getItemAll(): List<ItemEntity> {
        return itemDao.getItemAll()
    }

    override suspend fun getItemsCount(): Flow<Int> {
        return itemDao.getItemsCount()
    }

}