package com.squirrel.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.squirrel.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(account:AccountEntity)

    @Update
    suspend fun update(account:AccountEntity)

    @Delete
    suspend fun delete(account:AccountEntity)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>
}