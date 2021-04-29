package com.humansuit.yourweather.model

import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.CurrentWeatherResponse
import io.reactivex.rxjava3.core.Single

class WeatherModel(private val weatherApi: OpenWeatherService?) {

    fun getCurrentWeather(location: String): Single<CurrentWeatherResponse>? {
        return weatherApi?.getCurrentWeatherData(location)
    }

}