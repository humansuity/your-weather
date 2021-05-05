package com.humansuit.yourweather.model

import android.content.SharedPreferences
import android.location.Geocoder
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.view.MainContract
import io.reactivex.rxjava3.core.Single

class WeatherModel(
    private val weatherApi: OpenWeatherService?,
    sharedPreferences: SharedPreferences,
    private val geocoder: Geocoder
) : MainContract.Model(sharedPreferences) {

    fun getCurrentWeather(latitude: Float, longitude: Float, units: String = "metric"): Single<WeatherStateResponse>? {
        return weatherApi?.getCurrentWeatherData(latitude, longitude, units)
    }

    fun getCityNameByLocation() : String {
        val location = getSavedLocation()
        val addressesList = geocoder.getFromLocation(
            location.first.toDouble(),
            location.second.toDouble(), 1)
        return addressesList[0].getAddressLine(0)?.substringAfter(',') ?: "Undefined"
    }

}