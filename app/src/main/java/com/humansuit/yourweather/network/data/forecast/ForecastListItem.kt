package com.humansuit.yourweather.network.data.forecast

import com.humansuit.yourweather.network.data.current_weather.MainWeatherState
import com.humansuit.yourweather.network.data.current_weather.Weather

data class ForecastListItem(
    val date: Long,
    val mainWeatherState: MainWeatherState,
    val weather: List<Weather>
)
