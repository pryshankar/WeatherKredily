package com.android.weatherkredily

import android.app.Application
import androidx.room.Room
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.MIGRATION_13_14
import com.android.weatherkredily.data.remote.NetworkService
import com.android.weatherkredily.data.remote.Networking
import com.android.weatherkredily.di.component.ApplicationComponent
import com.android.weatherkredily.di.component.DaggerApplicationComponent
import com.android.weatherkredily.di.module.ApplicationModule

class WeatherApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }


    private fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.BASE_URL
        )


    private fun provideDatabaseService(): DatabaseService = Room.databaseBuilder(
        this,
        DatabaseService::class.java,
        "weather-database-db"
    ).addMigrations(MIGRATION_13_14).build()


    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

}