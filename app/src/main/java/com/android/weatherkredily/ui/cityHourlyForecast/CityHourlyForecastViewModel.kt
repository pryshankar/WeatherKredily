package com.android.weatherkredily.ui.cityHourlyForecast

import com.android.weatherkredily.base.BaseViewModel
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.remote.repository.WeatherRepository
import com.android.weatherkredily.utils.network.NetworkHelper
import com.android.weatherkredily.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CityHourlyForecastViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val weatherRepository: WeatherRepository,
    private val databaseService : DatabaseService
) : BaseViewModel(schedulerProvider,compositeDisposable, networkHelper) {


    override fun onCreate() {
        TODO("Not yet implemented")
    }
}