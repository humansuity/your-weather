package com.humansuit.yourweather.view.current_weather

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.FragmentCurrentWeatherBinding
import com.humansuit.yourweather.model.WeatherModel
import com.humansuit.yourweather.view.data.CurrentWeatherState
import com.humansuit.yourweather.network.OpenWeatherService
import com.humansuit.yourweather.utils.ActivityStateObserver
import com.humansuit.yourweather.view.MainContract
import com.humansuit.yourweather.utils.OPEN_WEATHER_API
import com.humansuit.yourweather.utils.getWeatherStateIcon
import com.humansuit.yourweather.utils.showErrorScreen
import com.humansuit.yourweather.view.data.ErrorState
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather), CurrentWeatherView {

    private val viewBinding: FragmentCurrentWeatherBinding by viewBinding()
    private var presenter: MainContract.Presenter? = null
    private var shareButton: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val weatherModel = WeatherModel(
            getWeatherApi(), sharedPreferences,
            Geocoder(requireContext(), Locale.getDefault())
        )
        initUiComponents()
        setPresenter(CurrentWeatherPresenter(this, weatherModel))
        presenter?.onViewCreated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(
            ActivityStateObserver(
            onCreateStateCallback = { setEnableUi(enable = false) })
        )
    }

    override fun onDetach() {
        presenter?.onViewDetach()
        super.onDetach()

        /**
         *
         *     Can't understand why this method called when you switch
         *     from ForecastFragment to this one. So that i must wrap presenter with nullable,
         *     otherwise the app keeps crashing because of nullable presenter :(
         *
         */
    }

    override fun showWeatherState(weatherState: CurrentWeatherState) {
        with(viewBinding.weatherWidgetContainer) {
            humidityText.text = weatherState.humidity
            pressureText.text = weatherState.pressure
            windSpeedText.text = weatherState.windSpeed
            windDirectionText.text = weatherState.windDirection
            rainfallText.text = weatherState.rainfall
        }
        with(viewBinding) {
            temperature.text = weatherState.temperature
            this.weatherState.text = weatherState.weatherState
            currentLocation.text = weatherState.location
            weatherStateIcon.setImageResource(getWeatherStateIcon(weatherState.weatherState))
        }
    }

    override fun setEnableUi(enable: Boolean) {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.menu.forEach { it.isEnabled = enable }
        shareButton?.isEnabled = enable
    }


    override fun showProgress(show: Boolean) {
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }


    override fun showErrorScreen(error: ErrorState) {
        requireActivity().showErrorScreen(error)
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }


    private fun initUiComponents() {
        viewBinding.shareButton.setOnClickListener { onShareButtonClick() }
        shareButton = viewBinding.shareButton
    }


    private fun onShareButtonClick() {
        val currentWeatherState = getCurrentWeatherState()
        shareTextWeather(currentWeatherState.toString())
    }


    private fun shareTextWeather(textWeatherState: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textWeatherState)
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(
            shareIntent,
            "Select an app you want to share weather"
        )
        startActivity(chooserIntent)
    }


    private fun getCurrentWeatherState(): CurrentWeatherState {
        with(viewBinding) {
            return CurrentWeatherState(
                location = currentLocation.text as String,
                weatherState = weatherState.text as String,
                humidity = weatherWidgetContainer.humidityText.text as String,
                pressure = weatherWidgetContainer.pressureText.text as String,
                temperature = temperature.text as String,
                windSpeed = weatherWidgetContainer.windSpeedText.text as String,
                windDirection = weatherWidgetContainer.windDirectionText.text as String,
                rainfall = weatherWidgetContainer.rainfallText.text as String,
            )
        }
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