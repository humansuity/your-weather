package com.humansuit.yourweather.view.current_weather

import android.util.Log
import com.humansuit.yourweather.utils.MainContract
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CurrentWeatherPresenter(view: CurrentWeatherView,
                              private val weatherModel: WeatherModel)
    : MainContract.Presenter {

    private var view: CurrentWeatherView? = view

    override fun onViewCreated() {
        loadCurrentWeather(location = "Vitebsk")
    }
    override fun onViewDetach() {
        view = null
    }

    private fun loadCurrentWeather(location: String) {
        weatherModel.getCurrentWeather(location)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { view?.showProgress(true) }
            ?.doFinally { view?.showProgress(false) }
            ?.subscribe(object: DisposableSingleObserver<WeatherStateResponse>() {
                override fun onSuccess(response: WeatherStateResponse?) {
                    response?.let { view?.showWeatherState(it) }
                    Log.d("Network", "Got success result from openweatherapi.com!")
                }

                override fun onError(e: Throwable?) {
                    Log.e("Network", "Something went wrong:\n${e?.message}")
                }
            })
    }

}