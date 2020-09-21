package com.android.weatherkredily.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class City(

    @Expose
    @SerializedName("id")
    var cityId: Long,

    @Expose
    @SerializedName("name")
    var cityName: String
) : Parcelable