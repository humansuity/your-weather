package com.humansuit.yourweather.view.current_weather

import android.util.Log
import com.humansuit.yourweather.MainContract
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.data.CurrentWeatherResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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

    fun loadCurrentWeather(location: String) {
        weatherModel.getCurrentWeather(location)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnSubscribe { view?.showProgress(true) }
            ?.delay(1000, TimeUnit.MILLISECONDS)
            ?.doFinally { view?.showProgress(false) }
            ?.subscribe(object: DisposableSingleObserver<CurrentWeatherResponse>() {
                override fun onSuccess(value: CurrentWeatherResponse?) {
                    value?.weatherState?.let { view?.showWeatherState(it) }
                }

                override fun onError(e: Throwable?) {
                    Log.e("Network", "Something went wrong:\n${e?.message}")
                }
            })
    }

}