package com.humansuit.yourweather.di

import com.humansuit.yourweather.model.ForecastWeatherModel
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModelModule {

    @Provides
    @Singleton
    fun provideWeatherModel(
        weatherApi: OpenWeatherService,
        appPreference: AppPreferenceProvider,
        geocoder: GeocoderProvider
    ) : WeatherModel {
        return  WeatherModel(weatherApi, appPreference, geocoder)
    }

    @Provides
    @Singleton
    fun provideForecastWeatherModel(
        weatherApi: OpenWeatherService,
        appPreference: AppPreferenceProvider
    ) : ForecastWeatherModel {
        return  ForecastWeatherModel(weatherApi, appPreference)
    }

}