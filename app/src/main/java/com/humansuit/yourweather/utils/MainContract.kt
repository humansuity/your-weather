package com.humansuit.yourweather.utils

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