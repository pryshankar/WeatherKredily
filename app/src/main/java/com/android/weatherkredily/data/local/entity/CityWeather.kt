package com.android.weatherkredily.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cityWeatherTable")
data class CityWeather(

    @PrimaryKey
    var cityId: Long = 0,

    @ColumnInfo(name = "cityName")
    var cityName: String,

    @ColumnInfo(name = "temperature")
    var temperature: String,

    @ColumnInfo(name = "isNight")
    var isNight: Boolean,

    @ColumnInfo(name = "isCurrentLocation")
    var isCurrentLocation: Boolean,

    @ColumnInfo(name = "weatherIcon" , typeAffinity = ColumnInfo.BLOB )
    var weatherIcon: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CityWeather

        if (cityId != other.cityId) return false
        if (cityName != other.cityName) return false
        if (temperature != other.temperature) return false
        if (isNight != other.isNight) return false
        if (isCurrentLocation != other.isCurrentLocation) return false
        if (!weatherIcon.contentEquals(other.weatherIcon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cityId.hashCode()
        result = 31 * result + cityName.hashCode()
        result = 31 * result + temperature.hashCode()
        result = 31 * result + isNight.hashCode()
        result = 31 * result + isCurrentLocation.hashCode()
        result = 31 * result + weatherIcon.contentHashCode()
        return result
    }
}