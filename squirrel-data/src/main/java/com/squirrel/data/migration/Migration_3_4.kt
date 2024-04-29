package com.squirrel.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 创建临时表 transaction_tmp
        db.execSQL(
            """CREATE TABLE `transaction_tmp` (
                `itemId` TEXT NOT NULL UNIQUE,
                `dayId` TEXT NOT NULL,
                `year` INTEGER NOT NULL,
                `month` INTEGER NOT NULL,
                `day` INTEGER NOT NULL,
                `time` TEXT NOT NULL,
                `remark` TEXT NOT NULL,
                `amount` REAL NOT NULL DEFAULT 0.0,
                `account` INTEGER NOT NULL,
                `category` INTEGER NOT NULL,
                `timestamp` INTEGER NOT NULL,
                PRIMARY KEY(`itemId`),
                FOREIGN KEY(`dayId`) REFERENCES `days` (`dayId`) ON DELETE CASCADE,
                FOREIGN KEY(`account`) REFERENCES `accounts` (`id`) ON DELETE CASCADE)"""
                .trimIndent()
        )

        // 复制旧表信息到临时表
        db.execSQL(
            """
                INSERT INTO transaction_tmp(itemId, dayId, year, month, day, remark, time, amount, 
                account, timestamp, category) 
                SELECT itemId, dayId, year, month, day, remark, time, amount, account, timestamp, 
                category FROM transactions
            """.trimIndent()
        )

        // 更新account表中totalAmount字段
        db.execSQL(
            """
                UPDATE accounts SET totalAmount = CASE 
                WHEN name = "现金" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 0),0.0) 
                WHEN name = "支付宝" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 1),0.0)
                WHEN name = "微信" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 2),0.0)
                WHEN name = "云闪付" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 3),0.0)
                WHEN name = "银行卡" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 4),0.0)
                WHEN name = "花呗" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 5),0.0)
                WHEN name = "京东白条" THEN ifnull((SELECT SUM(amount) FROM transactions WHERE account = 6),0.0)
                ELSE 0.0
                END
            """.trimIndent()
        )

        // 删除旧表
        db.execSQL(
            """
                DROP TABLE transactions
            """.trimIndent()
        )

        // 修改临时表表为旧表名
        db.execSQL(
            """
               ALTER TABLE transaction_tmp RENAME TO transactions
            """.trimIndent()
        )

        // 添加索引
        db.execSQL(
            """
                CREATE INDEX index_transactions_dayId ON transactions (dayId)
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE INDEX index_transactions_account ON transactions (account)
            """.trimIndent()
        )
    }
}