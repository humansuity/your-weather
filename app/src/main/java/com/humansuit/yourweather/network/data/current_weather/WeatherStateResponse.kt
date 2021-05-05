package com.humansuit.yourweather.network.data.current_weather

import com.squareup.moshi.Json

data class WeatherStateResponse(
    @field:Json(name = "main") val mainWeatherState: MainWeatherState,
    val wind: Wind,
    val weather: List<Weather>,
    val rain: Rain?
) {
    fun getNullRainfall() = "0.0 mm"
}