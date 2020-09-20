package com.android.weatherkredily.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main(
    @Expose
    @SerializedName("temp")
    var temperature: Float,
    @Expose
    @SerializedName("feels_like")
    var feelTemperature: Float,
    @Expose
    @SerializedName("temp_min")
    var minTemperature: Float,
    @Expose
    @SerializedName("temp_max")
    var maxTemperature: Float
)