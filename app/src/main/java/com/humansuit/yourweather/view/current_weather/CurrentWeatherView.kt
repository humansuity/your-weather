package com.humansuit.yourweather.view.current_weather

import com.humansuit.yourweather.model.data.CurrentWeatherState
import com.humansuit.yourweather.utils.MainContract
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse

interface CurrentWeatherView : MainContract.View<MainContract.Presenter> {
    fun showWeatherState(weatherState: CurrentWeatherState)
}