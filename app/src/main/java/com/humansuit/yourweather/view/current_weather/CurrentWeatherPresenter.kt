package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.R
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.model.data.CurrentWeatherState
import com.humansuit.yourweather.model.data.ErrorState
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.utils.ErrorList
import com.humansuit.yourweather.view.MainContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class CurrentWeatherPresenter(view: CurrentWeatherView,
                              private val weatherModel: WeatherModel)
    : MainContract.Presenter {


    private val disposable = CompositeDisposable()
    private var view: CurrentWeatherView? = view

    override fun onViewCreated() {
        loadCurrentWeather()
    }

    override fun onViewDetach() {
        disposable.dispose()
        view = null
    }

    private fun loadCurrentWeather() {
        try {
            val location = weatherModel.getSavedLocation()
            disposable.add(weatherModel.getCurrentWeather(location.first, location.second)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { view?.showProgress(true) }
                ?.doFinally { view?.showProgress(false); view?.setEnableUi(true) }
                ?.subscribeWith(object : DisposableSingleObserver<WeatherStateResponse>() {

                    override fun onSuccess(response: WeatherStateResponse?) {
                        doOnSuccessWeatherResponse(response)
                    }

                    override fun onError(e: Throwable?) {
                        doOnErrorWeatherResponse(e)
                    }

                })
            )
        } catch (e: IllegalStateException) {
            view?.showErrorScreen(
                ErrorState(ErrorList.UNDEFINED.state, R.drawable.ic_error)
            )
        }
    }

    private fun doOnSuccessWeatherResponse(response: WeatherStateResponse?) {
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
        else view?.showErrorScreen(
            ErrorState(ErrorList.UNDEFINED.state, R.drawable.ic_error)
        )
    }

    private fun doOnErrorWeatherResponse(e: Throwable?) {
        if (e?.message != null) view?.showErrorScreen(
            ErrorState(ErrorList.NETWORK.state, R.drawable.ic_no_internet_icon)
        )
        else view?.showErrorScreen(
            ErrorState(ErrorList.NETWORK.state, R.drawable.ic_no_internet_icon)
        )
    }

}