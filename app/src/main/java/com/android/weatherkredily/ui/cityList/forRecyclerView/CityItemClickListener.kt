package com.android.weatherkredily.ui.cityList.forRecyclerView

interface CityItemClickListener {
    fun onCityClicked(cityId : Long)
    fun onDeleteCityClicked(cityId : Long, itemAdapterPosition : Int)
}