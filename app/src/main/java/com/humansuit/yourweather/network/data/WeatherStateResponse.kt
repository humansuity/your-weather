package com.humansuit.yourweather.network.data

import com.squareup.moshi.Json

data class WeatherStateResponse(
    @field:Json(name = "main") val mainWeatherState: MainWeatherState,
    val wind: Wind,
    val weather: List<Weather>
)