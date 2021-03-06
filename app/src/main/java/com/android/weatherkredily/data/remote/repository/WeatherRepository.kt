package com.android.weatherkredily.data.remote.repository

import com.android.weatherkredily.data.remote.NetworkService
import com.android.weatherkredily.data.remote.response.CityCurrentWeatherResponse
import com.android.weatherkredily.data.remote.response.CityHourlyForecastResponse
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepository @Inject constructor(
     val networkService: NetworkService
) {

    fun fetchCurrentLocationWeather(latitude: String?, longitude: String?): Single<CityCurrentWeatherResponse> {
        return networkService.doGetCurrentLocationWeatherApiCall(
            latitude,
            longitude
        )
    }

    fun fetchCityWeatherById(id: String?): Single<CityCurrentWeatherResponse> {
        return networkService.doGetCityWeatherApiCallById(
            id
        )
    }


    fun fetchCityWeatherByName(name: String?): Single<CityCurrentWeatherResponse> {
        return networkService.doGetCityWeatherApiCallByName(
            name
        )
    }


    fun fetchCityHourlyForecast(id: Long?): Single<CityHourlyForecastResponse> {
        return networkService.doGetCityHourlyForecastApiCall(
            id
        )
    }


    fun fetchWeatherIcon(icon: String?): Single<ByteArray> {
        return networkService.getWeatherIcon(
            icon
        )
    }


}