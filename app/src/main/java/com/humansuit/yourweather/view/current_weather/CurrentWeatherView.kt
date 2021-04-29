package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.MainContract
import com.humansuit.yourweather.network.data.WeatherState

interface CurrentWeatherView : MainContract.View<MainContract.Presenter> {
    fun showWeatherState(weatherState: WeatherState)
    fun showProgress(show: Boolean)
}