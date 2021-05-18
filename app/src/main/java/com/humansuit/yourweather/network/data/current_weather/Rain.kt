package com.humansuit.yourweather.network.data.current_weather

import com.squareup.moshi.Json

data class Rain (
    @field:Json(name = "1h") val oneHourRainfall: Float = 0.0F,
    @field:Json(name = "3h") val threeHourRainfall: Float = 0.0F
) {
    fun getOneHourRainfall() = "$oneHourRainfall mm"
    fun getThreeHourRainfall() = "$threeHourRainfall mm"
}
