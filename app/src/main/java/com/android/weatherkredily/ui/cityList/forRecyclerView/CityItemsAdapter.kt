package com.android.weatherkredily.ui.cityList.forRecyclerView

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherkredily.R
import com.android.weatherkredily.data.local.Converter
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.utils.common.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

class CityItemsAdapter(private val cityWeatherList: List<CityWeather>, private val cityItemClickListener: CityItemClickListener)
    : RecyclerView.Adapter<CityItemViewHolder>() {


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder = CityItemViewHolder(R.layout.item_city,parent)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder {
       val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
       return CityItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = cityWeatherList.size

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) {
         holder.cityName.text = cityWeatherList[position].cityName
         holder.cityTemp.text = cityWeatherList[position].temperature

         holder.parentLayout.setOnClickListener {
             cityItemClickListener.onCityClicked()
         }


        val glideRequest = Glide.with(holder.weatherIcon.context)
            .load(Constants.ICON_URL_PREFIX + cityWeatherList[0].weatherIcon + Constants.ICON_URL_SUFFIX_SMALL_IMAGE)
        glideRequest.into(holder.weatherIcon)
        glideRequest.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                //holder.weatherIcon.setImageBitmap(Converter.fromByteArrayToImage(cityWeatherList[position].weatherIcon))
                Glide.with(holder.weatherIcon.context).load(Converter.fromByteArrayToImage(cityWeatherList[position].weatherIcon)).into(holder.weatherIcon)
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
    }

}