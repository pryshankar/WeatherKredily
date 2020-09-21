package com.android.weatherkredily.ui.cityList.forRecyclerView

interface CityItemClickListener {
    fun onCityClicked(cityId : Long, cityName : String)
    fun onDeleteCityClicked(cityId : Long, itemAdapterPosition : Int)
}