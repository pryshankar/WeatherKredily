package com.android.weatherkredily.utils

import com.android.weatherkredily.data.local.entity.CityWeather

class SortByLocation : Comparator<CityWeather> {

    override fun compare(a: CityWeather, b: CityWeather): Int {
          return sortByLocation(a.isCurrentLocation, b.isCurrentLocation)
    }

    private fun sortByLocation(a:Boolean,b:Boolean): Int{
        return b.compareTo(a)
    }


}