package com.squirrel.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squirrel.data.dao.AccountDao
import com.squirrel.data.dao.CombineDao
import com.squirrel.data.dao.DayDao
import com.squirrel.data.dao.TransactionDao
import com.squirrel.data.entity.AccountEntity
import com.squirrel.data.entity.DayEntity
import com.squirrel.data.entity.TransactionEntity
import com.squirrel.data.migration.migration_2_3
import com.squirrel.data.migration.migration_3_4
import com.squirrel.utils.Constants

@Database(
    entities = [TransactionEntity::class, DayEntity::class, AccountEntity::class],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = SquirrelRoomDatabase.Migration1To2::class
        ),
        AutoMigration(
            from = 2,
            to = 3,
        ),
        AutoMigration(
            from = 3,
            to = 4,
        ),
    ],
    version = 4,
    exportSchema = true,
)
abstract class SquirrelRoomDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun dayDao(): DayDao
    abstract fun combineDao(): CombineDao
    abstract fun accountDao(): AccountDao

    companion object {
        @Volatile
        private var Instance: SquirrelRoomDatabase? = null

        fun getDatabase(context: Context): SquirrelRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, SquirrelRoomDatabase::class.java, Constants.DATABASE_NAME
                )
                    .addMigrations(migration_2_3, migration_3_4)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                """
                                INSERT INTO accounts (name,totalAmount) VALUES
                                ("现金",0),
                                ("支付宝",0),
                                ("微信",0),
                                ("云闪付",0),
                                ("银行卡",0),
                                ("花呗",0),
                                ("京东白条",0)
                                """.trimIndent()
                            )
                        }
                    })
                    .build().also { Instance = it }
            }
        }

        fun closeDatabase() {
            Instance?.close()
            Instance = null
        }
    }

    @RenameTable(fromTableName = "items", toTableName = "transactions")
    @RenameColumn.Entries(
        RenameColumn(
            tableName = "items",
            fromColumnName = "exp",
            toColumnName = "amount"
        ),
        RenameColumn(
            tableName = "days",
            fromColumnName = "dayExpenditure",
            toColumnName = "totalAmount"
        )

    )
    class Migration1To2 : AutoMigrationSpec


}

