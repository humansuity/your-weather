package com.humansuit.yourweather.network.data

import com.squareup.moshi.Json
import kotlin.math.roundToInt

data class Wind(
    val speed: Float,
    @field:Json(name = "deg") val degree: Int
) {
    fun getRoundedSpeed() = speed.roundToInt().toString() + " km/h"
}
