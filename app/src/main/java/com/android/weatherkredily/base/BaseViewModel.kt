package com.android.weatherkredily.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.weatherkredily.R
import com.android.weatherkredily.utils.common.Resource
import com.android.weatherkredily.utils.network.NetworkHelper
import com.android.weatherkredily.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(
    val schedulerProvider: SchedulerProvider,
    val compositeDisposable: CompositeDisposable,
    val networkHelper: NetworkHelper

) : ViewModel() {

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()
    val messageString: MutableLiveData<Resource<String>> = MutableLiveData()

    protected fun checkInternetConnectionWithMessage(): Boolean =
        if (networkHelper.isNetworkConnected()) {
            true
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            false
        }

    protected fun checkInternetConnection(): Boolean = networkHelper.isNetworkConnected()

    abstract fun onCreate()


    protected fun handleError(error: String?) =
        error?.let {
            messageString.postValue(Resource.error(it))
        }

}