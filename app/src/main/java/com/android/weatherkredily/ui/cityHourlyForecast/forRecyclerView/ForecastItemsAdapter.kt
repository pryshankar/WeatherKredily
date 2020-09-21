package com.android.weatherkredily.ui.cityHourlyForecast.forRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherkredily.R
import com.android.weatherkredily.data.model.HourlyForecast
import com.android.weatherkredily.utils.common.Constants
import com.android.weatherkredily.utils.common.TempUtils
import com.bumptech.glide.Glide
import javax.inject.Inject

class ForecastItemsAdapter  @Inject constructor(private val hourlyForecastList: ArrayList<HourlyForecast>)
    : RecyclerView.Adapter<ForecastItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
       val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
       return ForecastItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = hourlyForecastList.size

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {

         holder.apply {

             dateAndTime.text = hourlyForecastList[position].dateAndTime
             averageTemp.text = averageTemp.context.getString(R.string.get_temp_in_degree_celsius, TempUtils.getTempInCelsius(hourlyForecastList[position].main.temperature))
             minTemp.text = minTemp.context.getString(R.string.get_temp_in_degree_celsius, TempUtils.getTempInCelsius(hourlyForecastList[position].main.minTemperature))
             maxTemp.text = maxTemp.context.getString(R.string.get_temp_in_degree_celsius, TempUtils.getTempInCelsius(hourlyForecastList[position].main.maxTemperature))

             Glide.with(weatherIcon.context)
                 .load(Constants.ICON_URL_PREFIX + hourlyForecastList[position].weatherList[0].weatherIcon + Constants.ICON_URL_SUFFIX_LARGE_IMAGE)
                 .into(weatherIcon)
         }

    }

}