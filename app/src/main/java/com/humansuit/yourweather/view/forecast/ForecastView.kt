package com.humansuit.yourweather.view.forecast

import com.humansuit.yourweather.view.data.ForecastSection
import com.humansuit.yourweather.view.MainContract

interface ForecastView : MainContract.View<MainContract.Presenter> {

    fun updateForecastList(forecastList: List<ForecastSection>)

}