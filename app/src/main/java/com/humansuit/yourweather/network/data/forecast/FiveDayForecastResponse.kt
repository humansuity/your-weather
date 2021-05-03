package com.humansuit.yourweather.network.data.forecast

import com.squareup.moshi.Json

data class FiveDayForecastResponse(
        @field:Json(name = "list") val forecastList: List<ForecastListItem>
)
