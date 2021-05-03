package com.humansuit.yourweather.view.forecast

import android.util.Log
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.utils.MainContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class ForecastPresenter(view: ForecastView,
                        private val model: WeatherModel) : MainContract.Presenter {

    private var view: ForecastView? = view

    override fun onViewCreated() {
        loadForecast(location = "Vitebsk")
    }

    override fun onViewDetach() {
        view = null
    }

    private fun loadForecast(location: String) {
        model.getFiveDayForecast(location)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { view?.showProgress(true) }
            ?.doFinally { view?.showProgress(false) }
            ?.subscribe(object: DisposableSingleObserver<FiveDayForecastResponse>() {
                override fun onSuccess(response: FiveDayForecastResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onError(e: Throwable?) {
                    Log.e("Network", "Something went wrong:\n${e?.message}")
                }
            })
    }

}