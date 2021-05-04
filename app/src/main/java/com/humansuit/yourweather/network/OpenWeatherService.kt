package com.humansuit.yourweather.network

import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val apikey = "c3823f2792a29fd380a58e3a0b8e7f60"

interface OpenWeatherService {

    @GET("weather?appid=$apikey")
    fun getCurrentWeatherData(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String
    ) : Single<WeatherStateResponse>

    @GET("forecast?appid=$apikey")
    fun getFiveDayForecast(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("cnt") timestamps: Int,
        @Query("units") units: String
    ) : Single<FiveDayForecastResponse>

}
