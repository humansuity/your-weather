package com.humansuit.yourweather.view.forecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.utils.MainContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException

class ForecastPresenter(view: ForecastView,
                        private val weatherModel: WeatherModel) : MainContract.Presenter {

    private var view: ForecastView? = view

    override fun onViewCreated() {
        loadFiveDayForecast()
    }

    override fun onViewDetach() {
        view = null
    }

    private fun loadFiveDayForecast() {
        try {
            val location = weatherModel.getSavedLocation()
            weatherModel.getFiveDayForecast(location.first, location.second)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { view?.showProgress(true) }
                ?.doFinally { view?.showProgress(false) }
                ?.subscribe(object: DisposableSingleObserver<FiveDayForecastResponse>() {
                    override fun onSuccess(response: FiveDayForecastResponse?) {
                        if (response?.forecastList != null) {
                            val forecastSectionList = weatherModel.getParsedForecast(response.forecastList)
                            view?.updateForecastList(forecastSectionList)
                        } else view?.showErrorScreen("Something went wrong")
                    }

                    override fun onError(e: Throwable?) {
                        if (e?.message != null) view?.showErrorScreen(e.message!!)
                        else view?.showErrorScreen("Something went wrong")
                    }
                })
        } catch(e: IllegalStateException) {
            view?.showErrorScreen("Something went wrong")
        }

    }

}