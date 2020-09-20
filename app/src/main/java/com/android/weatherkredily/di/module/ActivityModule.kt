package com.android.weatherkredily.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weatherkredily.base.BaseActivity
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.data.remote.repository.WeatherRepository
import com.android.weatherkredily.di.ActivityScope
import com.android.weatherkredily.ui.cityList.CityListActivity
import com.android.weatherkredily.ui.cityList.CityListViewModel
import com.android.weatherkredily.ui.cityList.forRecyclerView.CityItemClickListener
import com.android.weatherkredily.utils.ViewModelProviderFactory
import com.android.weatherkredily.utils.network.NetworkHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity : BaseActivity<*>) {

     @Provides
     fun provideCityListViewModel(
         compositeDisposable: CompositeDisposable,
         networkHelper: NetworkHelper,
         weatherRepository: WeatherRepository,
         databaseService: DatabaseService
     ):CityListViewModel  = ViewModelProviders.of(
    activity, ViewModelProviderFactory(CityListViewModel::class) {
        CityListViewModel(compositeDisposable,networkHelper, weatherRepository,databaseService  )
    }).get(CityListViewModel::class.java)


    @Provides
    fun provideFusedLocationProviderClient() : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)


    @Provides
    fun provideLinearLayoutManager():LinearLayoutManager = LinearLayoutManager(activity)

    @ActivityScope
    @Provides
    fun provideCityWeatherArrayList() : ArrayList<CityWeather> = ArrayList()

    @Provides
    fun provideCityItemClickListener() : CityItemClickListener = activity as CityListActivity


}