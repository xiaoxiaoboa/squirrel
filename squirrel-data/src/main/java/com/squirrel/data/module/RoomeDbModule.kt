package com.squirrel.data.module

import android.content.Context
import com.squirrel.data.SquirrelRoomDatabase
import com.squirrel.data.dao.DayDao
import com.squirrel.data.dao.EntireDao
import com.squirrel.data.dao.ItemDao
import com.squirrel.data.repository.DayRepository
import com.squirrel.data.repository.EntireRepository
import com.squirrel.data.repository.ItemRepository
import com.squirrel.data.repository.impl.DayRepositoryImpl
import com.squirrel.data.repository.impl.EntireRepositoryImpl
import com.squirrel.data.repository.impl.ItemRepositoryImpl
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
    fun provideItemDao(db: SquirrelRoomDatabase): ItemDao {
        return db.itemDao()
    }

    @Provides
    fun provideDayDao(db: SquirrelRoomDatabase): DayDao {
        return db.dayDao()
    }



    @Provides
    fun provideEntireDao(db: SquirrelRoomDatabase): EntireDao {
        return db.entireDao()
    }

    @Provides
    @Singleton
    fun provideItemRepository(itemDao: ItemDao): ItemRepository {
        return ItemRepositoryImpl(itemDao)
    }

    @Provides
    @Singleton
    fun provideDayRepository(dayDao: DayDao): DayRepository {
        return DayRepositoryImpl(dayDao)
    }



    @Provides
    @Singleton
    fun provideEntireRepository(entireDao: EntireDao): EntireRepository {
        return EntireRepositoryImpl(entireDao)
    }
}