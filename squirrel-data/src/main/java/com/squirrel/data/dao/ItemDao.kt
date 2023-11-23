package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.squirrel.data.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
//    @Insert
//    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity): Int

    @Delete
    suspend fun delete(item: ItemEntity): Int

    @Transaction
    @Query(
        "SELECT * FROM items WHERE items.itemId = :itemId"
    )
    suspend fun getItemById(itemId: Int): ItemEntity

    @Transaction
    @Query("SELECT * FROM items")
    suspend fun getItemAll(): List<ItemEntity>

    @Transaction
    @Query("SELECT COUNT(*) FROM items")
    fun getItemsCount(): Flow<Int>
}