package com.humansuit.yourweather.view

import android.content.SharedPreferences
import com.humansuit.yourweather.utils.KEY_PREFERENCES_LATITUDE
import com.humansuit.yourweather.utils.KEY_PREFERENCES_LONGITUDE
import com.humansuit.yourweather.model.data.ErrorState
import java.lang.IllegalStateException

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

    abstract class Model(private val sharedPreferences: SharedPreferences) {

        fun getSavedLocation(): Pair<Float, Float> {
            if (sharedPreferences.contains(KEY_PREFERENCES_LATITUDE) && sharedPreferences.contains(
                    KEY_PREFERENCES_LONGITUDE)) {
                val latitude = sharedPreferences.getString(KEY_PREFERENCES_LATITUDE, "0")?.toFloat()!!
                val longitude = sharedPreferences.getString(KEY_PREFERENCES_LONGITUDE, "0")?.toFloat()!!
                return Pair(latitude, longitude)
            } else throw IllegalStateException("Something went wrong!")
        }

    }

}