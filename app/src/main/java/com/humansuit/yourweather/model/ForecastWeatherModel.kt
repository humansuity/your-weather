package com.humansuit.yourweather.model

import com.humansuit.yourweather.di.AppPreferenceProvider
import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.network.data.forecast.ForecastListItem
import com.humansuit.yourweather.utils.WeekDayList
import com.humansuit.yourweather.utils.getWeatherStateIcon
import com.humansuit.yourweather.view.MainContract
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

class ForecastWeatherModel @Inject constructor(
    private val weatherApi: OpenWeatherService?,
    sharedPreferences: AppPreferenceProvider,
) : MainContract.Model(sharedPreferences) {

    fun getFiveDayForecast(
        latitude: Float,
        longitude: Float,
        timestamps: Int = 40,
        units: String = "metric"
    ): Single<FiveDayForecastResponse>? {
        return weatherApi?.getFiveDayForecast(latitude, longitude, timestamps, units)
    }

    fun getForecastSectionList(forecastList: List<ForecastListItem>): List<ForecastSection> {
        val dailyForecastList = parseToDailyForecastList(forecastList)
        return parseToForecastSectionList(dailyForecastList)
    }

    private fun parseToForecastSectionList(
        dailyForecastList: MutableMap<String, ArrayList<ForecastSection.WeatherState>>
    ): List<ForecastSection> {
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

    private fun parseToDailyForecastList(forecastList: List<ForecastListItem>)
    : MutableMap<String, ArrayList<ForecastSection.WeatherState>> {
        val dailyForecastList = mutableMapOf<String, ArrayList<ForecastSection.WeatherState>>()
        forecastList.forEach { forecastItem ->
            val weatherState = ForecastSection.WeatherState(
                time = forecastItem.getForecastedHour(),
                state = forecastItem.weather[0].main,
                degree = forecastItem.mainWeatherState.getTemperatureWithMark(),
                icon = getWeatherStateIcon(forecastItem.weather[0].main)
            )
            setWeatherStateByDay(
                dailyForecastList,
                dayOfWeek = forecastItem.getForecastedWeekDay(),
                weatherState
            )
        }
        return dailyForecastList
    }

    private fun setWeatherStateByDay(
        dailyForecastList: MutableMap<String, ArrayList<ForecastSection.WeatherState>>,
        dayOfWeek: Int,
        weatherState: ForecastSection.WeatherState
    ) {
        val dayOfWeekString = WeekDayList.values().find { it.number == dayOfWeek }?.name ?: "UNDEFINED"
        if (dailyForecastList[dayOfWeekString] == null) {
            dailyForecastList[dayOfWeekString] = arrayListOf()
        }
        dailyForecastList[dayOfWeekString]?.add(weatherState)
    }


}