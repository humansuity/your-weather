package com.humansuit.yourweather.network.data

import com.squareup.moshi.Json

data class Wind(
    val speed: Float,
    @field:Json(name = "deg") val degree: Int
)
