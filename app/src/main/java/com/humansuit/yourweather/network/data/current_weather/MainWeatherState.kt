package com.humansuit.yourweather.network.data.current_weather

import com.squareup.moshi.Json
import kotlin.math.roundToInt

data class MainWeatherState (
    @field:Json(name = "temp") private val temperature : Float,
    @field:Json(name = "temp_min") val minTemperature : Float,
    @field:Json(name = "temp_max") val maxTemperature : Float,
    @field:Json(name = "feels_like") val feelsLike : Float,
    @field:Json(name = "pressure") private val pressure : String,
    @field:Json(name = "humidity") private val humidity : String
) {
    fun getRoundedTemperature() = temperature.roundToInt().toString() + " °C"
    fun getHumidity() = "$humidity%"
    fun getPressure() = "$pressure hPa"
}
