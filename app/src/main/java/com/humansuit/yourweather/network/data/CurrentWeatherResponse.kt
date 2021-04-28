package com.humansuit.yourweather.network.data

import com.squareup.moshi.Json

data class CurrentWeatherResponse(
    @field:Json(name = "main") val weatherState: WeatherState,
    val wind: Wind,
    val weather: List<Weather>
)