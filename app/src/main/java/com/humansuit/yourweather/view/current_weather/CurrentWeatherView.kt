package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.MainContract
import com.humansuit.yourweather.network.data.WeatherStateResponse

interface CurrentWeatherView : MainContract.View<MainContract.Presenter> {
    fun showWeatherState(weatherState: WeatherStateResponse)
    fun showProgress(show: Boolean)
}