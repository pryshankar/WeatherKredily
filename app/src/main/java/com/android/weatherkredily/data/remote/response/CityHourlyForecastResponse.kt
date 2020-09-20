package com.android.weatherkredily.data.remote.response

import com.android.weatherkredily.data.model.Main
import com.android.weatherkredily.data.model.Weather
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityHourlyForecastResponse(
    @Expose
    @SerializedName("weather")
    var weatherList: List<Weather>,

    @Expose
    @SerializedName("main")
    var main: Main,

    @Expose
    @SerializedName("dt")
    var time: Long,

    @Expose
    @SerializedName("name")
    var nameOfCity: String
)