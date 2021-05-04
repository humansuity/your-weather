package com.humansuit.yourweather.network.data.forecast

import com.humansuit.yourweather.network.data.current_weather.MainWeatherState
import com.humansuit.yourweather.network.data.current_weather.Weather
import com.squareup.moshi.Json
import java.util.*

data class ForecastListItem(
        @field:Json(name = "dt") val date: Long,
        @field:Json(name = "main") val mainWeatherState: MainWeatherState,
        val weather: List<Weather>,
        @field:Json(name = "dt_txt") val textDate: String
) {
    private fun getTimeInMillis() = date * 1000
    fun getDate() = Calendar.getInstance().also { it.timeInMillis = getTimeInMillis() }
    fun getForecastedHour() = getDate().get(Calendar.HOUR_OF_DAY).toString() + ":00"
    fun getForecastedWeekDay() = getDate().get(Calendar.DAY_OF_WEEK)
}
