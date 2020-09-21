package com.android.weatherkredily.ui.cityHourlyForecast


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.weatherkredily.R
import com.android.weatherkredily.base.BaseActivity
import com.android.weatherkredily.data.local.entity.CityWeather
import com.android.weatherkredily.data.model.City
import com.android.weatherkredily.data.model.HourlyForecast
import com.android.weatherkredily.di.component.ActivityComponent
import com.android.weatherkredily.ui.cityHourlyForecast.forRecyclerView.ForecastItemsAdapter
import com.android.weatherkredily.utils.common.Status
import kotlinx.android.synthetic.main.activity_city_hourly_forecast.*
import javax.inject.Inject

class CityHourlyForecastActivity : BaseActivity<CityHourlyForecastViewModel>() {


    @Inject
    lateinit var linearLayoutManager : LinearLayoutManager

    @Inject
    lateinit var forecastItemsAdapter: ForecastItemsAdapter

    @Inject
    lateinit var hourlyForecastList : ArrayList<HourlyForecast>


    override fun provideLayoutId(): Int = R.layout.activity_city_hourly_forecast

    override fun setupView(savedInstanceState: Bundle?) {
        hourlyForecastsRecyclerView.layoutManager = linearLayoutManager
        hourlyForecastsRecyclerView.adapter = forecastItemsAdapter

        val city : City?  = intent.extras?.getParcelable(getString(R.string.cityDetails))

        if (city != null) {
            supportActionBar?.title = city.cityName
            viewModel.loadForecastsForCityUsingCityId(city.cityId)
        }
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            loadingPb.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.hourlyForecastsList.observe(this, Observer {
            if(it.status==Status.ERROR){
                showMessage(getString(R.string.something_went_wrong))
            }else if(it.status==Status.SUCCESS){
                it.data?.let { it1 -> hourlyForecastList.addAll(it1) }
                forecastItemsAdapter.notifyDataSetChanged()
            }
        })
    }


}