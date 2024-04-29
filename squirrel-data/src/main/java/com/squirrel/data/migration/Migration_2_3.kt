package com.squirrel.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS accounts (
            `id` INTEGER NOT NULL UNIQUE,
            `name` TEXT NOT NULL,
            `totalAmount` REAL NOT NULL,
            PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO accounts (name,totalAmount) VALUES
            ("现金",0.0),
            ("支付宝",0.0),
            ("微信",0.0),
            ("云闪付",0),
            ("银行卡",0),
            ("花呗",0),
            ("京东白条",0)
            """.trimIndent()
        )
    }
}