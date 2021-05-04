package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.view.data.CurrentWeatherState
import com.humansuit.yourweather.view.MainContract

interface CurrentWeatherView : MainContract.View<MainContract.Presenter> {
    fun showWeatherState(weatherState: CurrentWeatherState)
}