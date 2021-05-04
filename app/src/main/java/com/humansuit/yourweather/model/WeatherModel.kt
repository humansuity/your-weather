package com.humansuit.yourweather.model

import android.content.SharedPreferences
import android.location.Geocoder
import com.humansuit.yourweather.R
import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.network.data.forecast.ForecastListItem
import io.reactivex.rxjava3.core.Single
import java.lang.IllegalStateException
import java.util.*

class WeatherModel(
    private val weatherApi: OpenWeatherService?,
    private val sharedPreferences: SharedPreferences,
    private val geocoder: Geocoder
) {

    fun getCurrentWeather(latitude: Float, longitude: Float, units: String = "metric"): Single<WeatherStateResponse>? {
        return weatherApi?.getCurrentWeatherData(latitude, longitude, units)
    }

    fun getFiveDayForecast(latitude: Float, longitude: Float, timestamps: Int = 40, units: String = "metric"): Single<FiveDayForecastResponse>? {
        return weatherApi?.getFiveDayForecast(latitude, longitude, timestamps, units)
    }

    fun getParsedForecast(forecastList: List<ForecastListItem>): List<ForecastSection> {
        val dailyForecastList = getDailyForecastList(forecastList)
        return getForecastSectionList(dailyForecastList)
    }

    fun getSavedLocation(): Pair<Float, Float> {
        if (sharedPreferences.contains("latitude") && sharedPreferences.contains("longitude")) {
            val latitude = sharedPreferences.getString("latitude", "0")?.toFloat()!!
            val longitude = sharedPreferences.getString("longitude", "0")?.toFloat()!!
            return Pair(latitude, longitude)
        } else throw IllegalStateException("Something went wrong!")
    }

    fun getCityNameByLocation() : String {
        val location = getSavedLocation()
        val addressesList = geocoder.getFromLocation(
            location.first.toDouble(),
            location.second.toDouble(), 1)
        return addressesList[0].getAddressLine(0) ?: "Undefined"
    }


    private fun getForecastSectionList(dailyForecastList: MutableMap<String, ArrayList<ForecastSection.WeatherState>>)
            : List<ForecastSection> {
        val forecastSectionList = arrayListOf<ForecastSection>()
        dailyForecastList.keys.forEach { dayKey ->
            val dayOfWeek = ForecastSection.WeekDay(dayKey)
            forecastSectionList.add(dayOfWeek)
            dailyForecastList[dayKey]?.forEach { weatherState ->
                forecastSectionList.add(weatherState)
            }
        }
        return forecastSectionList
    }


    private fun getDailyForecastList(forecastList: List<ForecastListItem>)
            : MutableMap<String, ArrayList<ForecastSection.WeatherState>> {
        val dailyForecastList =  mutableMapOf<String, ArrayList<ForecastSection.WeatherState>>()
        forecastList.forEach { forecastItem ->
            val forecastDate = forecastItem.getDate()
            val weatherState = ForecastSection.WeatherState(
                    time = forecastDate.get(Calendar.HOUR_OF_DAY).toString() + ":00",
                    state = forecastItem.weather[0].main,
                    degree = forecastItem.mainWeatherState.getTemperatureWithMark(),
                    icon = R.drawable.ic_sun
            )
            when(forecastDate.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> { setWeatherStateByDay(dailyForecastList, "MONDAY", weatherState) }
                Calendar.TUESDAY -> { setWeatherStateByDay(dailyForecastList, "TUESDAY", weatherState) }
                Calendar.WEDNESDAY -> { setWeatherStateByDay(dailyForecastList, "WEDNESDAY", weatherState) }
                Calendar.THURSDAY -> { setWeatherStateByDay(dailyForecastList, "THURSDAY", weatherState) }
                Calendar.FRIDAY -> { setWeatherStateByDay(dailyForecastList, "FRIDAY", weatherState) }
                Calendar.SATURDAY -> { setWeatherStateByDay(dailyForecastList, "SATURDAY", weatherState) }
                Calendar.SUNDAY -> { setWeatherStateByDay(dailyForecastList, "SUNDAY", weatherState) }
            }
        }
        return dailyForecastList
    }


    private fun setWeatherStateByDay(
            dailyForecastList: MutableMap<String, ArrayList<ForecastSection.WeatherState>>,
            dayOfWeek: String,
            weatherState: ForecastSection.WeatherState)
    {
        if (dailyForecastList[dayOfWeek] == null) {
            dailyForecastList[dayOfWeek] = arrayListOf()
        }
        dailyForecastList[dayOfWeek]?.add(weatherState)
    }

}