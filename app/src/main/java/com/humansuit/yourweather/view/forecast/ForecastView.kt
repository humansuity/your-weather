package com.humansuit.yourweather.view.forecast

import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.utils.MainContract

interface ForecastView : MainContract.View<MainContract.Presenter> {

    fun updateForecastList(forecastList: List<ForecastSection>)

}