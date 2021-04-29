package com.humansuit.yourweather.network

import com.humansuit.yourweather.network.data.WeatherStateResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val apikey = "c3823f2792a29fd380a58e3a0b8e7f60"

interface OpenWeatherService {

    @GET("weather?units=metric&appid=$apikey")
    fun getCurrentWeatherData(
        @Query("q") location: String
    ) : Single<WeatherStateResponse>
}
