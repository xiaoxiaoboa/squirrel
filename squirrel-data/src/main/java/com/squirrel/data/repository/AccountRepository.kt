package com.squirrel.data.repository

import com.squirrel.data.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun insert(account:AccountEntity)

    suspend fun update(account:AccountEntity)

    suspend fun delete(account: AccountEntity)

    suspend fun getAllAccounts(): Flow<List<AccountEntity>>

}