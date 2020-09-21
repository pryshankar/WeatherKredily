package com.android.weatherkredily.ui.cityList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weatherkredily.R
import com.android.weatherkredily.base.BaseActivity
import com.android.weatherkredily.data.local.Converter
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.data.model.City
import com.android.weatherkredily.data.remote.response.CityCurrentWeatherResponse
import com.android.weatherkredily.di.component.ActivityComponent
import com.android.weatherkredily.ui.cityHourlyForecast.CityHourlyForecastActivity
import com.android.weatherkredily.ui.cityList.forRecyclerView.CityItemClickListener
import com.android.weatherkredily.ui.cityList.forRecyclerView.CityItemsAdapter
import com.android.weatherkredily.utils.common.Constants
import com.android.weatherkredily.utils.common.Resource
import com.android.weatherkredily.utils.common.Status
import com.android.weatherkredily.utils.common.showAddCityDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_city_list.*
import kotlinx.android.synthetic.main.activity_city_list.loadingPb
import javax.inject.Inject


class CityListActivity : BaseActivity<CityListViewModel>() , CityItemClickListener, View.OnClickListener, OnCitySubmitListener {

     @Inject
     lateinit var fusedLocationClient: FusedLocationProviderClient

     @Inject
     lateinit var linearLayoutManager : LinearLayoutManager

     @Inject
     lateinit var cityItemsAdapter: CityItemsAdapter

     @Inject
     lateinit var citiesList : ArrayList<CityWeather>

    override fun provideLayoutId(): Int  = R.layout.activity_city_list

    override fun setupView(savedInstanceState: Bundle?) {

        cityRv.layoutManager = linearLayoutManager
        cityRv.adapter = cityItemsAdapter

        addCityFab.setOnClickListener(this)

        getLatitudeAndLongitude()
    }


    private fun getLatitudeAndLongitude() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permissions have not been granted
            requestPermissions()
            return
        }


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.

                if (location == null) {
                    viewModel.run {
                        latitude.postValue(Resource.error())
                        longitude.postValue(Resource.error())
                    }

                } else {
                    viewModel.run {
                        latitude.setValue(Resource.success(location.latitude.toString()))
                        longitude.setValue(Resource.success(location.longitude.toString()))
                    }
                }
            }
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.LOCATION_PERMISSION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.LOCATION_PERMISSION -> {

                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted()
                } else if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        val showRationale = shouldShowRequestPermissionRationale(permissions[0])

                        if (!showRationale) {
                            //Permission Denied Permanently for Marshmallow and above devices
                            showMessage(R.string.permission_denied_permanently)
                        } else {
                            // Permission Denied Temporarily  for Marshmallow and above devices
                            showMessage(R.string.permission_denied_temporarily)
                        }

                    } else {
                        // Permission Denied for Marshmallow and lower devices
                        showMessage(R.string.permission_denied_temporarily)
                    }
                }
                return
            }
        }
    }


    private fun onPermissionGranted() {
        getLatitudeAndLongitude()
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            loadingPb.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.listOfCitiesWithCurrentWeatherOffline.observe(this, Observer {
            citiesList.clear()
            it.data?.let { it1 -> citiesList.addAll(it1) }
            cityItemsAdapter.notifyDataSetChanged()
        })

        viewModel.newCityAdded.observe(this, Observer {
            it.data?.let { it1 -> citiesList.add(it1) }
            cityItemsAdapter.notifyItemInserted(citiesList.size-1)
            linearLayoutManager.scrollToPosition(citiesList.size-1)
        })

        //do not set observer to latitude because latitude is being setValue first.
        //As soon as latitude's value will be set, it's change will be observed and
        //loadCurrentWeather() will be called before longitude's value has been set
        viewModel.longitude.observe(this, Observer {
            viewModel.loadCurrentWeather()
        })

        viewModel.cityToBeUpdated.observe(this, Observer {
            it.data?.let { it1 -> downloadIconImage(it1) }
        })

        viewModel.isCityValid.observe(this, Observer {
            if(it.status == Status.ERROR){
                showMessage(getString(R.string.city_already_added))
            }else if(it.status == Status.UNKNOWN){

                if(it.data.equals(Constants.CITY_NOT_FOUND)){
                    showMessage(getString(R.string.city_does_not_exist))
                }else if(it.data.equals(Constants.SOMETHING_WENT_WRONG)) {
                    showMessage(getString(R.string.something_went_wrong))
                }
            }
        })

        viewModel.deletedCityPosition.observe(this, Observer {
            if(it.status == Status.SUCCESS){
                it.data?.let { it1 ->
                    citiesList.removeAt(it1)
                    cityItemsAdapter.notifyItemRemoved(it1)
                }
            }else if(it.status == Status.ERROR){
                showMessage(getString(R.string.deletion_failed))
            }
        })

    }

    override fun onCityClicked(cityId: Long, cityName : String) {
        val intent = Intent(this, CityHourlyForecastActivity::class.java)
        intent.putExtra(getString(R.string.cityDetails), City(cityId, cityName))
        startActivity(intent)
    }

    override fun onDeleteCityClicked(cityId: Long, itemAdapterPosition : Int) {
        viewModel.deleteCityFromDatabaseUsingCityId(cityId, itemAdapterPosition)
    }

    private fun downloadIconImage(response : CityCurrentWeatherResponse){
        Glide
            .with(applicationContext)
            .asBitmap()
            .load(Constants.ICON_URL_PREFIX + response.weatherList[0].weatherIcon + Constants.ICON_URL_SUFFIX_SMALL_IMAGE)
            .into(object : SimpleTarget<Bitmap?>(60, 60) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    viewModel.updateIconOfCityWeatherInDb(response.cityId,Converter.fromImageToByteArray(resource))
                }
            })
    }


    override fun onClick(view: View?) {
        if(view?.id == R.id.addCityFab){
            showAddCityDialog(this,false,this)
        }
    }

    override fun onCitySubmit(city: String) {
        viewModel.loadWeatherForCityByCityName(city)
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}









