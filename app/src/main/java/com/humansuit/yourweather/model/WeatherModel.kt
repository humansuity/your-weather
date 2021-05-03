package com.humansuit.yourweather.model

import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.network.data.forecast.ForecastListItem
import io.reactivex.rxjava3.core.Single

class WeatherModel(private val weatherApi: OpenWeatherService?) {

    fun getCurrentWeather(location: String, units: String = "metric"): Single<WeatherStateResponse>? {
        return weatherApi?.getCurrentWeatherData(location, units)
    }

    fun getFiveDayForecast(location: String, timestamps: Int = 3): Single<FiveDayForecastResponse>? {
        return weatherApi?.getFiveDayForecast(location, timestamps)
    }

    fun parseFiveDayForecast(forecastList: List<ForecastListItem>): List<ForecastSection> {
        val forecastSectionList = emptyList<ForecastSection>()
        for (i in forecastList.withIndex()) {

        }
        return emptyList()
    }

    fun deleteNightTimestamps(forecastList: List<ForecastListItem>) {
        for(i in forecastList.withIndex()) {

        }
    }

}