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
import javax.inject.Inject

class CityItemsAdapter  @Inject constructor( private val cityWeatherList: ArrayList<CityWeather>,  private val cityItemClickListener: CityItemClickListener)
    : RecyclerView.Adapter<CityItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder {
       val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
       return CityItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = cityWeatherList.size

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) {

         holder.apply {

             cityName.text = cityWeatherList[position].cityName

             cityTemp.text = cityTemp.context.getString(R.string.get_temp_in_degree_celsius,TempUtils.getTempInCelsius(cityWeatherList[position].temperature.toFloat()))

             parentLayout.setOnClickListener {
                 cityItemClickListener.onCityClicked(cityWeatherList[adapterPosition].cityId, cityWeatherList[adapterPosition].cityName)
             }

             if(cityWeatherList[position].isNight){
                 cityName.setTextColor(getWhiteColor(holder.cityName.context))
                 cityTemp.setTextColor(getWhiteColor(holder.cityName.context))
                 parentLayout.setBackgroundColor(getNightColor(holder.cityName.context))
             }else{
                 cityName.setTextColor(getBlackColor(holder.cityName.context))
                 cityTemp.setTextColor(getBlackColor(holder.cityName.context))
                 parentLayout.setBackgroundColor(getDayColor(holder.cityName.context))
             }

             if(cityWeatherList[position].isCurrentLocation){
                 deleteOrMyLocationIcon.setImageResource(R.drawable.ic_location_24)
             }else{
                 deleteOrMyLocationIcon.setImageResource(R.drawable.ic_delete_24)
             }

             deleteOrMyLocationIcon.setOnClickListener {
                 if(!cityWeatherList[adapterPosition].isCurrentLocation)
                      cityItemClickListener.onDeleteCityClicked(cityWeatherList[adapterPosition].cityId, adapterPosition)
             }

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

    }


    private fun getDayColor(context : Context): Int = context.resources.getColor(R.color.dayColor)

    private fun getNightColor(context : Context): Int = context.resources.getColor(R.color.nightColor)

    private fun getBlackColor(context : Context): Int = context.resources.getColor(R.color.black)

    private fun getWhiteColor(context : Context): Int = context.resources.getColor(R.color.white)

}