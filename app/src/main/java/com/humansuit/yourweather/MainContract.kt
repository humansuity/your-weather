package com.humansuit.yourweather

interface MainContract {

    interface View<T> {
        fun setPresenter(presenter: T)
    }

    interface Presenter {
        fun onViewCreated()
        fun onViewDetach()
    }

}