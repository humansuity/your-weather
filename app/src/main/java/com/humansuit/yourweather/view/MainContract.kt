package com.humansuit.yourweather.view

import com.humansuit.yourweather.di.AppPreferenceProvider
import com.humansuit.yourweather.model.data.ErrorState
import com.humansuit.yourweather.utils.KEY_PREFERENCES_LATITUDE
import com.humansuit.yourweather.utils.KEY_PREFERENCES_LONGITUDE

interface MainContract {

    interface View<T> {
        fun setPresenter(presenter: T)
        fun showProgress(show: Boolean)
        fun showErrorScreen(error: ErrorState)
        fun setEnableUi(enable: Boolean)
    }

    interface Presenter {
        fun onViewCreated()
        fun onViewDetach()
    }

    abstract class Model(private val sharedPreferencesProvider: AppPreferenceProvider) {

        fun getSavedLocation(): Pair<Float, Float> {
            if (sharedPreferencesProvider.prefs.contains(KEY_PREFERENCES_LATITUDE)
                && sharedPreferencesProvider.prefs.contains(KEY_PREFERENCES_LONGITUDE)
            ) {
                val latitude = sharedPreferencesProvider.prefs
                    .getString(KEY_PREFERENCES_LATITUDE, "0")?.toFloat()!!
                val longitude = sharedPreferencesProvider.prefs
                    .getString(KEY_PREFERENCES_LONGITUDE, "0")?.toFloat()!!
                return Pair(latitude, longitude)
            } else throw IllegalStateException("Something went wrong!")
        }

    }

}