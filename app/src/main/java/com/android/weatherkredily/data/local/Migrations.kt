package com.android.weatherkredily.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_13_14 = object : Migration(13, 14) {

    override fun migrate(database: SupportSQLiteDatabase) {

//        val defaultValue = "\'01d\'"
//
//        val sqlQuery = "ALTER TABLE cityWeatherTable ADD COLUMN weatherIconUrlPart TEXT " +
//                               "DEFAULT " + defaultValue + " NOT NULL"
//
//        //database.execSQL("ALTER TABLE cityWeatherTable ADD COLUMN weatherIconUrlPart TEXT  NOT NULL")
//        database.execSQL(sqlQuery)
    }


}