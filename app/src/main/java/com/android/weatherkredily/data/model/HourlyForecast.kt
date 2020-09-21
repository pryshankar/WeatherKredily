package com.android.weatherkredily.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HourlyForecast(
    @Expose
    @SerializedName("dt_txt")
    var dateAndTime: String,

    @Expose
    @SerializedName("main")
    var main: Main,

    @Expose
    @SerializedName("weather")
    var weatherList: List<Weather>
)