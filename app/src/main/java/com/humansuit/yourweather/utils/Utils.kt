package com.humansuit.yourweather.utils

import android.content.Context
import android.location.Location
import androidx.preference.PreferenceManager
import com.humansuit.yourweather.R
import java.lang.IllegalArgumentException

const val TYPE_WEEKDAY = 0
const val TYPE_WEATHER_STATE = 1

const val OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/"

fun getWeatherStateIcon(weatherState: String) = try {
    WeatherStateIconRelation.valueOf(weatherState).icon
} catch (e: IllegalArgumentException) {
    R.drawable.ic_clouds_state
}

fun saveLastLocation(location: Location, context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = sharedPreferences.edit()
    editor.putString("latitude", location.latitude.toString())
    editor.putString("longitude", location.longitude.toString())
    editor.commit()
}