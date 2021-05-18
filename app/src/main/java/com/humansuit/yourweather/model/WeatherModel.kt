package com.humansuit.yourweather.model

import com.humansuit.yourweather.di.AppPreferenceProvider
import com.humansuit.yourweather.di.GeocoderProvider
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.view.MainContract
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherModel @Inject constructor(
    private val weatherApi: OpenWeatherService?,
    sharedPreferencesProvider: AppPreferenceProvider,
    private val geocoderProvider: GeocoderProvider
) : MainContract.Model(sharedPreferencesProvider) {

    fun getCurrentWeather(latitude: Float, longitude: Float, units: String = "metric")
            : Single<WeatherStateResponse>? {
        return weatherApi?.getCurrentWeatherData(latitude, longitude, units)
    }

    fun getCityNameByLocation(): String {
        val location = getSavedLocation()
        val addressesList = geocoderProvider.geocoder.getFromLocation(
            location.first.toDouble(),
            location.second.toDouble(), 1
        )
        return addressesList[0].getAddressLine(0)?.substringAfter(',') ?: "Undefined"
    }

}