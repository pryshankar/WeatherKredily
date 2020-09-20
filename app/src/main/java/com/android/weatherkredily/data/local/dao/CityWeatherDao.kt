package com.android.weatherkredily.data.local.dao

import androidx.room.*
import com.android.weatherkredily.data.local.entity.CityWeather
import io.reactivex.Single


@Dao
interface CityWeatherDao {

    @Insert
    fun insert(cityWeather: CityWeather): Single<Long>

    @Update
    fun update(cityWeather: CityWeather): Single<Int>

    @Query("UPDATE cityWeatherTable SET weatherIcon = :byteArray WHERE cityId = :cityId ")
    fun updateIcon(byteArray: ByteArray, cityId : Long ): Single<Int>

    @Delete
    fun delete(cityWeather: CityWeather): Single<Int>

    @Query("DELETE FROM  cityWeatherTable WHERE cityId = :cityId ")
    fun deleteCityUsingCityId(cityId: Long): Single<Int>

    @Query("DELETE FROM cityWeatherTable")
    fun deleteAll():Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(vararg weathers: CityWeather): Single<List<Long>>

    @Query("SELECT * FROM cityWeatherTable")
    fun getAllRows(): Single<List<CityWeather>>

    @Query("SELECT * from cityWeatherTable where cityId = :id LIMIT 1")
    fun getRowFromCityWeatherTableById(id: Long): Single<CityWeather>

    @Query("SELECT count(*) from cityWeatherTable")
    fun count(): Single<Int>
}