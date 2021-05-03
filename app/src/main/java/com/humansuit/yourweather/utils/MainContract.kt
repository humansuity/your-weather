package com.humansuit.yourweather.utils

import android.content.SharedPreferences

interface MainContract {

    interface View<T> {
        fun setPresenter(presenter: T)
        fun showProgress(show: Boolean)
    }

    interface Presenter {
        fun onViewCreated()
        fun onViewDetach()
    }

}