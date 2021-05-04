package com.humansuit.yourweather.view.data

sealed class ForecastSection {

    data class WeatherState (
        val time: String,
        val state: String,
        val degree: String,
        val icon: Int
    ) : ForecastSection()

    data class WeekDay(val day: String) : ForecastSection()

}
