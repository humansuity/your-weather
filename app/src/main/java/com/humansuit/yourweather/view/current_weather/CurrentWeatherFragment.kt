package com.humansuit.yourweather.view.current_weather

import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.humansuit.yourweather.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.humansuit.yourweather.utils.MainContract
import com.humansuit.yourweather.databinding.FragmentCurrentWeatherBinding
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.network.data.current_weather.WeatherStateResponse
import com.humansuit.yourweather.utils.OPEN_WEATHER_API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather), CurrentWeatherView {

    private val viewBinding: FragmentCurrentWeatherBinding by viewBinding()
    private lateinit var presenter: MainContract.Presenter
    private lateinit var weatherModel: WeatherModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        weatherModel = WeatherModel(getWeatherApi(), sharedPreferences)
        setPresenter(CurrentWeatherPresenter(this, weatherModel))
        presenter.onViewCreated()
    }


    override fun showWeatherState(weatherState: WeatherStateResponse) {
        with(viewBinding.weatherWidgetContainer) {
            humidityText.text = weatherState.mainWeatherState.getHumidity()
            pressureText.text = weatherState.mainWeatherState.getPressure()
            windSpeedText.text = weatherState.wind.getRoundedSpeed()
            windDirectionText.text = "SE"
            rainfallText.text = weatherState.rain?.getOneHourRainfall() ?: "0.0 mm"
        }
        viewBinding.degree.text = weatherState.mainWeatherState.getTemperatureWithCelsiumMark()
        viewBinding.weatherState.text = weatherState.weather[0].main
        viewBinding.currentLocation.text =
            weatherModel.getCityNameByLocation(Geocoder(requireContext(), Locale.getDefault()))
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
            .baseUrl(OPEN_WEATHER_API)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(OpenWeatherService::class.java)
    }




}