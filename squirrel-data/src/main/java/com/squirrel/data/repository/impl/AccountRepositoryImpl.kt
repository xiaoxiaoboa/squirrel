package com.squirrel.data.repository.impl

import com.squirrel.data.dao.AccountDao
import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDao: AccountDao) :
    AccountRepository {
    override suspend fun insert(account: AccountEntity) {
        accountDao.insert(account)
    }

    override suspend fun update(account: AccountEntity) {
        accountDao.update(account)
    }

    override suspend fun delete(account: AccountEntity) {
        accountDao.delete(account)
    }

    override suspend fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }
}