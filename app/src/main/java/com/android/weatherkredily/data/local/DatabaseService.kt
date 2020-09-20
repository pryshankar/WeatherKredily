package com.android.weatherkredily.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.weatherkredily.data.local.dao.CityWeatherDao
import com.android.weatherkredily.data.local.entity.CityWeather
import javax.inject.Singleton

@Singleton
@Database(
        entities = [
            CityWeather::class
        ],
        exportSchema = false,
        version = 14
)

abstract class DatabaseService : RoomDatabase() {

    abstract fun cityWeatherDao(): CityWeatherDao

}