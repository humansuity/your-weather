package com.humansuit.yourweather.network.data.current_weather

import com.squareup.moshi.Json
import kotlin.math.roundToInt

val WIND_DERICTIONS = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

data class Wind(
    val speed: Float,
    @field:Json(name = "deg") val degree: Int
) {
    fun getRoundedSpeed() = speed.roundToInt().toString() + " km/h"
    fun getWindDirection() = WIND_DERICTIONS[ ((speed.toDouble() % 360) / 45).roundToInt() % 8 ]
}
