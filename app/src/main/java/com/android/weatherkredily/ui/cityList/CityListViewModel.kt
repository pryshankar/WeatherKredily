package com.android.weatherkredily.ui.cityList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.weatherkredily.R
import com.android.weatherkredily.base.BaseViewModel
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.data.remote.repository.WeatherRepository
import com.android.weatherkredily.data.remote.response.CityCurrentWeatherResponse
import com.android.weatherkredily.utils.SortByLocation
import com.android.weatherkredily.utils.common.Constants
import com.android.weatherkredily.utils.common.Resource
import com.android.weatherkredily.utils.common.Status
import com.android.weatherkredily.utils.network.NetworkHelper
import com.android.weatherkredily.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class CityListViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    val weatherRepository: WeatherRepository,
    val databaseService: DatabaseService
) : BaseViewModel(schedulerProvider,compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "CityListViewModel"
    }

    val deletedCityPosition : MutableLiveData<Resource<Int>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val isCityValid : MutableLiveData<Resource<String>> = MutableLiveData()
    val listOfCitiesWithCurrentWeatherOffline: MutableLiveData<Resource<List<CityWeather>>> = MutableLiveData()
    val newCityAdded : MutableLiveData<Resource<CityWeather>> = MutableLiveData()
    val latitude: MutableLiveData<Resource<String>> = MutableLiveData()
    val longitude: MutableLiveData<Resource<String>> = MutableLiveData()

    val cityToBeUpdated : MutableLiveData<Resource<CityCurrentWeatherResponse>> = MutableLiveData()

    override fun onCreate() {}

    fun loadCurrentWeather() {
        if (checkInternetConnectionWithMessage()) {
            if (isLatAndLongValid()) {
                onLoadWeatherForCurrentLocation()
            }

        } else {
            loadAllFromDatabase()
        }
    }


    private fun onLoadWeatherForCurrentLocation() {

        lateinit var cityCurrentWeatherResponse : CityCurrentWeatherResponse

        loading.postValue(true)

        compositeDisposable.add(
            weatherRepository.fetchCurrentLocationWeather(latitude.value?.data,longitude.value?.data)
                .map {
                    cityCurrentWeatherResponse = it
                    //return@flatMap weatherRepository.fetchWeatherIcon(it.weatherList[0].weatherIcon)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe({

                    updateCityInDatabase(true,ByteArray(0),cityCurrentWeatherResponse)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            Log.d(TAG, "$it rows updated")

                            if(it==0){
                                insertCityInDatabase(cityCurrentWeatherResponse,true)
                                    .subscribeOn(schedulerProvider.io())
                                    .subscribe({
                                        Log.d(TAG, "$it is cityId for the row which got inserted")
                                        cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                        loadAllFromDatabase()
                                    },{
                                        loading.postValue(false)
                                        Log.d(TAG,it.message.toString())
                                    })
                            }else{
                                //this will help download icon image using url inside CityListActivity
                                cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                loadAllFromDatabase()
                            }

                        },{
                            loading.postValue(false)
                            Log.d(TAG,it.message.toString())
                        })

                },
                    {
                        loading.postValue(false)
                        Log.d(TAG,it.message.toString())
                    })

        )
    }


    private fun insertCityInDatabase(city : CityCurrentWeatherResponse, isCurrentLocation : Boolean) : Single<Long> {
        val cityWeather = CityWeather(cityId = city.cityId,
            cityName = city.nameOfCity,
            temperature = city.main.temperature.toString(),
            isNight = city.weatherList[0].weatherIcon.toLowerCase(Locale.US).contains("n"),
            isCurrentLocation = isCurrentLocation,
            weatherIconUrlPart = city.weatherList[0].weatherIcon,
            weatherIcon = ByteArray(0)
        )
        return databaseService.cityWeatherDao().insert(cityWeather)
    }


     fun loadWeatherForCityByCityName(cityName:String) {

         if(!checkInternetConnectionWithMessage()){
             return
         }

        lateinit var cityCurrentWeatherResponse : CityCurrentWeatherResponse

        compositeDisposable.add(
            weatherRepository.fetchCityWeatherByName(cityName)
                .map {
                    cityCurrentWeatherResponse = it
                    //return@flatMap weatherRepository.fetchWeatherIcon(it.weatherList[0].weatherIcon)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe({

                   databaseService.cityWeatherDao().getRowFromCityWeatherTableById(cityCurrentWeatherResponse.cityId)
                       .subscribeOn(schedulerProvider.io())
                       .subscribe({
                           isCityValid.postValue(Resource.error(Constants.CITY_ALREADY_EXISTS))
                       },{
                           insertCityInDatabase(cityCurrentWeatherResponse,false)
                               .subscribeOn(schedulerProvider.io())
                               .subscribe({
                                   Log.d(TAG,"Inserting in database Successful")
                                   //this will help download icon image using url inside CityListActivity
                                   cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                   loadCityFromDatabaseUsingCityId(cityCurrentWeatherResponse.cityId)
                               },{
                                   Log.d(TAG,it.message.toString())
                               })
                       })

                },
                    {
                        Log.d(TAG,it.message.toString())
                        if(it.message.toString().contains("404"))
                           isCityValid.postValue( Resource.unknown(Constants.CITY_NOT_FOUND))
                        else
                            isCityValid.postValue( Resource.unknown(Constants.SOMETHING_WENT_WRONG))
                    })

        )
    }

    private fun loadCityFromDatabaseUsingCityId(cityId: Long) {
        compositeDisposable.add(
            databaseService.cityWeatherDao().getRowFromCityWeatherTableById(cityId)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    newCityAdded.postValue(Resource.success(it))
                },{
                    Log.d(TAG,it.message.toString())
                })
        )
    }



    fun deleteCityFromDatabaseUsingCityId(cityId: Long, itemAdapterPosition : Int) {

        compositeDisposable.add(
            databaseService.cityWeatherDao().deleteCityUsingCityId(cityId)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    Log.d(TAG, "$it rows deleted")
                    deletedCityPosition.postValue(Resource.success(itemAdapterPosition))
                },{
                    deletedCityPosition.postValue(Resource.error())
                    Log.d(TAG,it.message.toString())
                })
        )
    }



    private fun loadAllFromDatabase() {

        loading.postValue(true)

        compositeDisposable.add(
            databaseService.cityWeatherDao().count()
                .map {
                    if (it > 0) {
                        compositeDisposable.add(
                            databaseService.cityWeatherDao()
                                .getAllRows()
                                .subscribeOn(schedulerProvider.io())
                                .subscribe({ listOfCities ->
                                    listOfCities.apply {
                                       Collections.sort(this,SortByLocation())
                                    }
                                    loading.postValue(false)
                                    listOfCitiesWithCurrentWeatherOffline.postValue(Resource.success(listOfCities))
                                }, {
                                    loading.postValue(false)
                                    Log.d(TAG,it.message.toString())
                                })
                        )
                    }
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {

                    }, {
                        loading.postValue(false)
                        Log.d(TAG,it.message.toString())
                    }
                )
        )
    }



    private fun updateCityInDatabase(isCurrentLocation : Boolean, byteArray : ByteArray ,city: CityCurrentWeatherResponse) : Single<Int> {

        val cityWeather = CityWeather(cityId = city.cityId,
            cityName = city.nameOfCity,
            temperature = city.main.temperature.toString(),
            isNight = city.weatherList[0].weatherIcon.toLowerCase(Locale.US).contains("n"),
            isCurrentLocation = isCurrentLocation,
            weatherIconUrlPart = city.weatherList[0].weatherIcon,
            weatherIcon = byteArray
        )

        return databaseService.cityWeatherDao().update(cityWeather)
    }



     fun updateIconOfCityWeatherInDb(cityId:Long, byteArray : ByteArray)  {

        compositeDisposable.add(
            databaseService.cityWeatherDao().updateIcon(byteArray,cityId)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    Log.d(TAG, "$it rows updated")
                },{
                    Log.d(TAG,it.message.toString())
                })
        )
    }




    private fun isLatAndLongValid(): Boolean {
        if (latitude.value?.status == Status.SUCCESS && longitude.value?.status == Status.SUCCESS) {
            messageStringId.postValue(Resource.error(R.string.location_available))
            return true
        } else {
            messageStringId.postValue(Resource.error(R.string.location_unavailable))
            return false
        }
    }

}