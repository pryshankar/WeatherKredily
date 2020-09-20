package com.android.weatherkredily.di.component

import android.app.Application
import android.content.Context
import com.android.weatherkredily.WeatherApplication
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.remote.NetworkService
import com.android.weatherkredily.di.ApplicationContext
import com.android.weatherkredily.di.module.ApplicationModule
import com.android.weatherkredily.utils.network.NetworkHelper
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent{

    fun inject(app:WeatherApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getDatabaseService(): DatabaseService

    fun getNetworkService(): NetworkService

    fun getNetworkHelper(): NetworkHelper

    fun getCompositeDisposable(): CompositeDisposable

}