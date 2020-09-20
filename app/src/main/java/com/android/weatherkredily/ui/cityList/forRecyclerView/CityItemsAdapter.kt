package com.android.weatherkredily.ui.cityList.forRecyclerView

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherkredily.R
import com.android.weatherkredily.data.local.Converter
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.utils.common.Constants
import com.android.weatherkredily.utils.common.TempUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class CityItemsAdapter(private val cityWeatherList: List<CityWeather>, private val cityItemClickListener: CityItemClickListener)
    : RecyclerView.Adapter<CityItemViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder {
       val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
       return CityItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = cityWeatherList.size

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) {
         holder.cityName.text = cityWeatherList[position].cityName
         holder.cityTemp.text = holder.cityTemp.context.getString(R.string.get_temp_in_degree_celsius,TempUtils.getTempInCelsius(cityWeatherList[position].temperature.toFloat()))

         holder.parentLayout.setOnClickListener {
             cityItemClickListener.onCityClicked()
         }


        val glideRequest = Glide.with(holder.weatherIcon.context)
            .load(Constants.ICON_URL_PREFIX + cityWeatherList[position].weatherIconUrlPart + Constants.ICON_URL_SUFFIX_LARGE_IMAGE)
            //.placeholder(R.drawable.ic_image)

        glideRequest.into(holder.weatherIcon)
        glideRequest.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {

                cityWeatherList[position].weatherIcon.let {
                    holder.weatherIcon.setImageBitmap(Converter.fromByteArrayToImage(it))
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }
        }).submit()


        if(cityWeatherList[position].isNight){
            holder.cityName.setTextColor(getWhiteColor(holder.cityName.context))
            holder.cityTemp.setTextColor(getWhiteColor(holder.cityName.context))
            holder.parentLayout.setBackgroundColor(getNightColor(holder.cityName.context))
        }else{
            holder.cityName.setTextColor(getBlackColor(holder.cityName.context))
            holder.cityTemp.setTextColor(getBlackColor(holder.cityName.context))
            holder.parentLayout.setBackgroundColor(getDayColor(holder.cityName.context))
        }
    }


    private fun getDayColor(context : Context): Int = context.resources.getColor(R.color.dayColor)

    private fun getNightColor(context : Context): Int = context.resources.getColor(R.color.nightColor)

    private fun getBlackColor(context : Context): Int = context.resources.getColor(R.color.black)

    private fun getWhiteColor(context : Context): Int = context.resources.getColor(R.color.white)

}