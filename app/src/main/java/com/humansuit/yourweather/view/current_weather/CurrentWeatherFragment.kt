package com.humansuit.yourweather.view.current_weather

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.humansuit.yourweather.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.humansuit.yourweather.MainContract
import com.humansuit.yourweather.databinding.FragmentCurrentWeatherBinding
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.WeatherStateResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather), CurrentWeatherView {

    private val viewBinding: FragmentCurrentWeatherBinding by viewBinding()
    private lateinit var presenter: MainContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPresenter(CurrentWeatherPresenter(this, WeatherModel(getWeatherApi())))
        initIcons()
        presenter.onViewCreated()
    }

    override fun showWeatherState(weatherState: WeatherStateResponse) {
        with(viewBinding.weatherWidgetContainer) {
            humidityText.text = weatherState.mainWeatherState.humidity.toString() + " %"
            pressureText.text = weatherState.mainWeatherState.pressure.toString() + " hPa"
            windSpeedText.text = weatherState.wind.getRoundedSpeed().toString() + " km/h"
            windDirectionText.text = "SE"
            rainfallText.text = "1.0 mm"
        }
        viewBinding.degree.text = weatherState.mainWeatherState.getRoundedTemperature().toString() + " °C"
        viewBinding.weatherState.text = weatherState.weather[0].main
    }

    fun initIcons() {
        with(viewBinding.weatherWidgetContainer) {
            humidityWidget.imageView.setImageResource(R.drawable.ic_humidity)
            rainWidget.imageView.setImageResource(R.drawable.ic_rainfall)
            pressureWidget.imageView.setImageResource(R.drawable.ic_pressure)
            windSpeedWidget.imageView.setImageResource(R.drawable.ic_wind)
            windDirectionWidget.imageView.setImageResource(R.drawable.ic_wind_direction)
        }
    }

    override fun showProgress(show: Boolean) {
        val progressBar = activity?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDetach() {
        presenter.onViewDetach()
        super.onDetach()
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