package com.android.weatherkredily.data.remote

import com.android.weatherkredily.data.remote.response.CityCurrentWeatherResponse
import com.android.weatherkredily.data.remote.response.CityHourlyForecastResponse
import io.reactivex.Single
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(Endpoints.CURRENT_WEATHER)
    fun doGetCurrentLocationWeatherApiCall(

        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query(Networking.API_KEY) apiKey: String = Networking.API_KEY_VALUE
    ): Single<CityCurrentWeatherResponse>



    @GET(Endpoints.CURRENT_WEATHER)
    fun doGetCityWeatherApiCallById(

        @Query("id") cityId: String?,
        @Query(Networking.API_KEY) apiKey: String = Networking.API_KEY_VALUE
    ): Single<CityCurrentWeatherResponse>


    @GET(Endpoints.CURRENT_WEATHER)
    fun doGetCityWeatherApiCallByName(

        @Query("q") cityName: String?,
        @Query(Networking.API_KEY) apiKey: String = Networking.API_KEY_VALUE
    ): Single<CityCurrentWeatherResponse>


    @GET(Endpoints.HOURLY_FORECAST)
    fun doGetCityHourlyForecastApiCall(

        @Query("id") cityId: String?,
        @Query(Networking.API_KEY) apiKey: String = Networking.API_KEY_VALUE
    ): Single<CityHourlyForecastResponse>

    //Openweathermap does not provide API for fetching image using url
    //not usable currently
    @GET(Endpoints.WEATHER_ICON)
    fun getWeatherIcon(
        @Path("icon") icon: String?

    ):Single<ByteArray>



}