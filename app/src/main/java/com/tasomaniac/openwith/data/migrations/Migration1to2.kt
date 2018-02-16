package com.tasomaniac.openwith.data.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

object Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE `openwith_temp` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `host` TEXT NOT NULL, `component` TEXT NOT NULL, `preferred` INTEGER NOT NULL, `last_chosen` INTEGER NOT NULL)"
        )
        database.execSQL(
            "INSERT INTO openwith_temp (_id, host, component, preferred, last_chosen) SELECT * FROM openwith"
        )
        database.execSQL("DROP TABLE openwith")
        database.execSQL("ALTER TABLE openwith_temp RENAME TO openwith")
        database.execSQL("CREATE UNIQUE INDEX `index_openwith_host` ON `openwith` (`host`)")
    }

}
