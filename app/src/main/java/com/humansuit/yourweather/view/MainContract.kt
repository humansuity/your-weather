package com.humansuit.yourweather.view

import android.content.SharedPreferences
import com.humansuit.yourweather.view.data.ErrorState
import java.lang.IllegalStateException

interface MainContract {

    interface View<T> {
        fun setPresenter(presenter: T)
        fun showProgress(show: Boolean)
        fun showErrorScreen(error: ErrorState)
    }

    interface Presenter {
        fun onViewCreated()
        fun onViewDetach()
    }

    abstract class Model(private val sharedPreferences: SharedPreferences) {

        fun getSavedLocation(): Pair<Float, Float> {
            if (sharedPreferences.contains("latitude") && sharedPreferences.contains("longitude")) {
                val latitude = sharedPreferences.getString("latitude", "0")?.toFloat()!!
                val longitude = sharedPreferences.getString("longitude", "0")?.toFloat()!!
                return Pair(latitude, longitude)
            } else throw IllegalStateException("Something went wrong!")
        }

    }

}