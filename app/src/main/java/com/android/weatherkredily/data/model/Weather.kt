package com.android.weatherkredily.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather(
    @Expose
    @SerializedName("description")
    var weatherDescription: String,
    @Expose
    @SerializedName("icon")
    var weatherIcon: String
)