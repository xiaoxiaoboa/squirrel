package com.squirrel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.squirrel.data.dao.DayDao
import com.squirrel.data.dao.EntireDao
import com.squirrel.data.dao.ItemDao
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.ItemEntity
import com.squirrel.utils.Constants

@Database(
    entities = [ItemEntity::class, DayEntity::class],
//    autoMigrations = [AutoMigration(from = 10, to = 11)],
    version = 11,
    exportSchema = false,
)
abstract class SquirrelRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun dayDao(): DayDao
    abstract fun entireDao(): EntireDao

    companion object {
        @Volatile
        private var Instance: SquirrelRoomDatabase? = null

        fun getDatabase(context: Context): SquirrelRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, SquirrelRoomDatabase::class.java, Constants.DATABASE_NAME
                ).build().also { Instance = it }
            }
        }

        fun closeDatabase() {
            Instance?.close()
            Instance = null
        }
    }
}

