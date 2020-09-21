package com.android.weatherkredily.ui.cityHourlyForecast.forRecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherkredily.R

class ForecastItemViewHolder (view : View) : RecyclerView.ViewHolder(view) {

    var dateAndTime: TextView = view.findViewById(R.id.dateAndTime)
    var averageTemp: TextView = view.findViewById(R.id.averageTemp)
    var minTemp: TextView = view.findViewById(R.id.minTemp)
    var maxTemp: TextView = view.findViewById(R.id.maxTemp)
    var weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)

}
