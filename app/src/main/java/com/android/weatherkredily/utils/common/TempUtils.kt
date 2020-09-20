package com.android.weatherkredily.utils.common


class TempUtils {

    fun getTempInKelvin(temp: String): Float =  ((temp.toFloat() + 273.15) + 1).toFloat()

    fun getTempInCelsius(temp: Float): String = ((temp - 273.15).toInt()).toString()
}