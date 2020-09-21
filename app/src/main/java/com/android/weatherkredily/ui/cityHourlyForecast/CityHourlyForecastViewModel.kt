package com.android.weatherkredily.ui.cityHourlyForecast

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.weatherkredily.base.BaseViewModel
import com.android.weatherkredily.data.model.HourlyForecast
import com.android.weatherkredily.data.remote.repository.WeatherRepository
import com.android.weatherkredily.data.remote.response.CityHourlyForecastResponse
import com.android.weatherkredily.ui.cityList.CityListViewModel
import com.android.weatherkredily.utils.common.Resource
import com.android.weatherkredily.utils.network.NetworkHelper
import com.android.weatherkredily.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class CityHourlyForecastViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    val weatherRepository: WeatherRepository
) : BaseViewModel(schedulerProvider,compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "CityHourlyForecastViewModel"
    }


    override fun onCreate() {
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val hourlyForecastsList: MutableLiveData<Resource<List<HourlyForecast>>> = MutableLiveData()

    fun loadForecastsForCityUsingCityId(cityId:Long) {
        if (checkInternetConnectionWithMessage()) {
            onLoadHourlyForecast(cityId)
        }
    }


    private fun onLoadHourlyForecast(cityId:Long){

        loading.postValue(true)

        lateinit var cityHourlyForecastResponse : CityHourlyForecastResponse

        compositeDisposable.add(

            weatherRepository.fetchCityHourlyForecast(cityId)
                .map{
                    cityHourlyForecastResponse = it
                    hourlyForecastsList.postValue(Resource.success(it.hourlyForecastList))
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    loading.postValue(false)
                    Log.d(CityListViewModel.TAG,it.toString())
                },{
                    loading.postValue(false)
                    hourlyForecastsList.postValue(Resource.error())
                    Log.d(CityListViewModel.TAG,it.message.toString())
                })

        )
    }

}