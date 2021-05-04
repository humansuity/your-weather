package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.utils.MainContract
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.model.data.CurrentWeatherState
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CurrentWeatherPresenter(view: CurrentWeatherView,
                              private val weatherModel: WeatherModel)
    : MainContract.Presenter {

    private var view: CurrentWeatherView? = view

    override fun onViewCreated() {
        loadCurrentWeather()
    }

    override fun onViewDetach() {
        view = null
    }

    private fun loadCurrentWeather() {
        try {
            val location = weatherModel.getSavedLocation()
            weatherModel.getCurrentWeather(location.first, location.second)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { view?.showProgress(true) }
                ?.doFinally { view?.showProgress(false) }
                ?.subscribe(object: DisposableSingleObserver<WeatherStateResponse>() {
                    override fun onSuccess(response: WeatherStateResponse?) {
                        if (response != null) {
                            with(response) {
                                val currWeatherState = CurrentWeatherState(
                                    location = weatherModel.getCityNameByLocation(),
                                    weatherState = weather[0].main,
                                    humidity = mainWeatherState.getHumidity(),
                                    pressure = mainWeatherState.getPressure(),
                                    temperature = mainWeatherState.getTemperatureWithCelsiumMark(),
                                    windSpeed = wind.getRoundedSpeed(),
                                    windDirection = wind.getWindDirection(),
                                    rainfall = rain?.getOneHourRainfall() ?: getNullRainfall()
                                )
                                view?.showWeatherState(currWeatherState)
                            }
                        }
                        else view?.showErrorScreen("Something went wrong")
                    }

                    override fun onError(e: Throwable?) {
                        if (e?.message != null) view?.showErrorScreen(e.message!!)
                        else view?.showErrorScreen("Something went wrong")
                    }
                })
        } catch (e: IllegalStateException) {
            view?.showErrorScreen("Something went wrong")
        }

    }

}