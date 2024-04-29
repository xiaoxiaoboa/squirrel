package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.squirrel.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
//    @Insert
//    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: TransactionEntity): Int

    @Delete
    suspend fun delete(item: TransactionEntity): Int

    @Transaction
    @Query(
        "SELECT * FROM transactions WHERE transactions.itemId = :itemId"
    )
    suspend fun getItemById(itemId: Int): TransactionEntity

    @Transaction
    @Query("SELECT * FROM transactions")
    suspend fun getItemAll(): List<TransactionEntity>

    @Transaction
    @Query("SELECT COUNT(*) FROM transactions")
    fun getItemsCount(): Flow<Int>
}