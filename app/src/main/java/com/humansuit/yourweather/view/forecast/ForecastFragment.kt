package com.humansuit.yourweather.view.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.FragmentForecastBinding
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.forecast.ForecastListItem
import com.humansuit.yourweather.utils.MainContract
import com.humansuit.yourweather.view.adapter.ForecastListAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ForecastFragment : Fragment(R.layout.fragment_forecast), ForecastView {

    val viewBinding: FragmentForecastBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weekDay = ForecastSection.WeekDay("TODAY")
        val weatherState = ForecastSection.WeatherState("13:00", "Sunny", "22°", R.drawable.ic_sun)
        val forecastList = mutableListOf<ForecastSection>()
        forecastList.add(weekDay)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weekDay)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)
        forecastList.add(weatherState)

        setPresenter(ForecastPresenter(this, WeatherModel(getWeatherApi())))
    }

    override fun updateForecastList(forecastList: List<ForecastSection>) {
        with(viewBinding) {
            recyclerView.adapter = ForecastListAdapter(forecastSectionList = forecastList)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        TODO("Not yet implemented")
    }

    override fun showProgress(show: Boolean) {
        TODO("Not yet implemented")
    }


    private fun getWeatherApi(): OpenWeatherService? {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(OpenWeatherService::class.java)
    }

}