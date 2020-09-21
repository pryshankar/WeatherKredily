package com.android.weatherkredily.di.component

import com.android.weatherkredily.di.ActivityScope
import com.android.weatherkredily.di.module.ActivityModule
import com.android.weatherkredily.ui.cityHourlyForecast.CityHourlyForecastActivity
import com.android.weatherkredily.ui.cityList.CityListActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {
    fun inject(activity: CityListActivity)

    fun inject(activity: CityHourlyForecastActivity)
}