package com.android.weatherkredily

import android.app.Application
import androidx.room.Room
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.MIGRATION_3_13
import com.android.weatherkredily.data.remote.NetworkService
import com.android.weatherkredily.data.remote.Networking

class WeatherApplication : Application() {

    lateinit var networkService : NetworkService
    lateinit var databaseService: DatabaseService

    override fun onCreate() {
        super.onCreate()

        networkService = provideNetworkService()
        databaseService = provideDatabaseService()
    }


    private fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.BASE_URL
        )


    private fun provideDatabaseService(): DatabaseService = Room.databaseBuilder(
        this,
        DatabaseService::class.java,
        "weather-database-db"
    ).addMigrations(MIGRATION_3_13).build()


}