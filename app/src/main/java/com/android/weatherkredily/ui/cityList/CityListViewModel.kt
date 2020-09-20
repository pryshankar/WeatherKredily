package com.android.weatherkredily.ui.cityList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.weatherkredily.R
import com.android.weatherkredily.base.BaseViewModel
import com.android.weatherkredily.data.local.DatabaseService
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.data.remote.repository.WeatherRepository
import com.android.weatherkredily.data.remote.response.CityCurrentWeatherResponse
import com.android.weatherkredily.utils.common.Resource
import com.android.weatherkredily.utils.common.Status
import com.android.weatherkredily.utils.network.NetworkHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class CityListViewModel(
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val weatherRepository: WeatherRepository,
    private val databaseService: DatabaseService
) : BaseViewModel(compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "CityListViewModel"
    }


    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val isCityValid : MutableLiveData<Resource<String>> = MutableLiveData()
    val listOfCitiesWithCurrentWeatherOffline: MutableLiveData<Resource<List<CityWeather>>> = MutableLiveData()
    val newCityAdded : MutableLiveData<Resource<CityWeather>> = MutableLiveData()
    val latitude: MutableLiveData<Resource<String>> = MutableLiveData()
    val longitude: MutableLiveData<Resource<String>> = MutableLiveData()

    val cityToBeUpdated : MutableLiveData<Resource<CityCurrentWeatherResponse>> = MutableLiveData()


    override fun onCreate() {
    }


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
                .subscribeOn(Schedulers.io())
                .subscribe({

                    updateCityInDatabase(true,ByteArray(0),cityCurrentWeatherResponse)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Log.d(TAG, "$it rows updated")

                            if(it==0){
                                insertCityInDatabase(cityCurrentWeatherResponse,true)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        Log.d(TAG, "$it is cityId for the row which got inserted")
                                        cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                        loadAllFromDatabase()
                                    },{
                                        Log.d(TAG,it.message.toString())
                                    })
                            }else{
                                //this will help download icon image using url inside CityListActivity
                                cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                loadAllFromDatabase()
                            }

                        },{
                            Log.d(TAG,it.message.toString())
                        })

                },
                    {
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
            weatherIcon = ByteArray(0)
        )
        return databaseService.cityWeatherDao().insert(cityWeather)
    }


    private fun onLoadWeatherForCityName(cityName:String) {

        lateinit var cityCurrentWeatherResponse : CityCurrentWeatherResponse

        loading.postValue(true)

        compositeDisposable.add(
            weatherRepository.fetchCityWeatherByName(cityName)
                .map {
                    cityCurrentWeatherResponse = it
                    //return@flatMap weatherRepository.fetchWeatherIcon(it.weatherList[0].weatherIcon)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({

                   databaseService.cityWeatherDao().getRowFromCityWeatherTableById(cityCurrentWeatherResponse.cityId)
                       .subscribeOn(Schedulers.io())
                       .subscribe({
                           isCityValid.value = Resource.error("City already exists")
                       },{
                           insertCityInDatabase(cityCurrentWeatherResponse,false)
                               .subscribeOn(Schedulers.io())
                               .subscribe({
                                   Log.d(TAG,"Inserting in database Successful")
                                   //this will help download icon image using url inside CityListActivity
                                   cityToBeUpdated.postValue(Resource.success(cityCurrentWeatherResponse))
                                   loadCityFromDatabaseUsingCityId(cityCurrentWeatherResponse.cityId)
                               },{

                               })
                       })

                },
                    {
                        isCityValid.value = Resource.unknown("Invalid City")
                    })

        )
    }

    private fun loadCityFromDatabaseUsingCityId(cityId: Long) {
        compositeDisposable.add(
            databaseService.cityWeatherDao().getRowFromCityWeatherTableById(cityId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    newCityAdded.postValue(Resource.success(it))
                },{
                    Log.d(TAG,it.message.toString())
                })
        )
    }


    private fun loadAllFromDatabase() {
        compositeDisposable.add(
            databaseService.cityWeatherDao().count()
                .map {
                    if (it > 0) {
                        compositeDisposable.add(
                            databaseService.cityWeatherDao()
                                .getAllRows()
                                .subscribeOn(Schedulers.io())
                                .subscribe({ listOfCities ->
                                         listOfCitiesWithCurrentWeatherOffline.postValue(Resource.success(listOfCities))
                                }, {
                                    Log.d(TAG,it.message.toString())
                                })
                        )
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {

                    }, {
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
            weatherIcon = byteArray
        )

        return databaseService.cityWeatherDao().update(cityWeather)
    }



     fun updateIconOfCityWeatherInDb(cityId:Long, byteArray : ByteArray)  {

        compositeDisposable.add(
            databaseService.cityWeatherDao().updateIcon(byteArray,cityId)
                .subscribeOn(Schedulers.io())
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