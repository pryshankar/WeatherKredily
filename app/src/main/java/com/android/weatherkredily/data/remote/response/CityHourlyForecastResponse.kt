package com.android.weatherkredily.data.remote.response

import com.android.weatherkredily.data.model.City
import com.android.weatherkredily.data.model.HourlyForecast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityHourlyForecastResponse(
    @Expose
    @SerializedName("list")
    var hourlyForecastList: List<HourlyForecast>,

    @Expose
    @SerializedName("city")
    var city: City

)