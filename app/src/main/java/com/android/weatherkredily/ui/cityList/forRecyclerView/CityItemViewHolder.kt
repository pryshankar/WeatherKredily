package com.android.weatherkredily.ui.cityList.forRecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherkredily.R

class CityItemViewHolder (view : View) : RecyclerView.ViewHolder(view) {

    var parentLayout : ConstraintLayout = view.findViewById(R.id.itemParentLayout)
    var cityName: TextView = view.findViewById(R.id.cityName)
    var cityTemp: TextView = view.findViewById(R.id.cityTemp)
    var weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)
}
