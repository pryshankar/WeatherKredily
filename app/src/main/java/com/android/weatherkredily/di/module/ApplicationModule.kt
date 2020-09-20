package com.android.weatherkredily.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.android.weatherkredily.BuildConfig
import com.android.weatherkredily.WeatherApplication
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.MIGRATION_13_14
import com.android.weatherkredily.data.remote.NetworkService
import com.android.weatherkredily.data.remote.Networking
import com.android.weatherkredily.di.ApplicationContext
import com.android.weatherkredily.utils.network.NetworkHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application : WeatherApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application


    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.BASE_URL
        )


    @Singleton
    @Provides
    fun provideDatabaseService() : DatabaseService = Room.databaseBuilder(
        application,
        DatabaseService::class.java,
        "weather-database-db"
    ).addMigrations(MIGRATION_13_14).build()


    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)

}