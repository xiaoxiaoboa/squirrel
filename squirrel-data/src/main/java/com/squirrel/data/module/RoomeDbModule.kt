package com.squirrel.data.module

import android.content.Context
import com.squirrel.data.SquirrelRoomDatabase
import com.squirrel.data.dao.AccountDao
import com.squirrel.data.dao.CombineDao
import com.squirrel.data.dao.DayDao
import com.squirrel.data.dao.TransactionDao
import com.squirrel.data.repository.AccountRepository
import com.squirrel.data.repository.CombineRepository
import com.squirrel.data.repository.DayRepository
import com.squirrel.data.repository.TransactionRepository
import com.squirrel.data.repository.impl.AccountRepositoryImpl
import com.squirrel.data.repository.impl.CombineRepositoryImpl
import com.squirrel.data.repository.impl.DayRepositoryImpl
import com.squirrel.data.repository.impl.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    @Provides
    @Singleton
    fun provideSquirrelRoomDatabase(
        @ApplicationContext appContext: Context
    ): SquirrelRoomDatabase {
        return SquirrelRoomDatabase.getDatabase(
            context = appContext
        )
    }

    @Provides
    fun provideTransactionDao(db: SquirrelRoomDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    fun provideDayDao(db: SquirrelRoomDatabase): DayDao {
        return db.dayDao()
    }


    @Provides
    fun provideCombineDao(db: SquirrelRoomDatabase): CombineDao {
        return db.combineDao()
    }

    @Provides
    fun provideAccountDao(db: SquirrelRoomDatabase): AccountDao {
        return db.accountDao()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(itemDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(itemDao)
    }

    @Provides
    @Singleton
    fun provideDayRepository(dayDao: DayDao): DayRepository {
        return DayRepositoryImpl(dayDao)
    }


    @Provides
    @Singleton
    fun provideCombineRepository(entireDao: CombineDao): CombineRepository {
        return CombineRepositoryImpl(entireDao)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(accountDao: AccountDao): AccountRepository {
        return AccountRepositoryImpl(accountDao)
    }
}