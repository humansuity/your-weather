package com.humansuit.yourweather.network.data

import com.squareup.moshi.Json

data class WeatherState (
    @field:Json(name = "temp") val temperature : Double,
    @field:Json(name = "temp_min") val minTemperature : Double,
    @field:Json(name = "temp_max") val maxTemperature : Double,
    @field:Json(name = "feels_like") val feelsLike : Double,
    @field:Json(name = "pressure") val pressure : Int,
    @field:Json(name = "humidity") val humidity : Int
)
