package com.humansuit.yourweather.view.forecast

import com.humansuit.yourweather.R
import com.humansuit.yourweather.model.ForecastWeatherModel
import com.humansuit.yourweather.network.data.forecast.FiveDayForecastResponse
import com.humansuit.yourweather.utils.ErrorList
import com.humansuit.yourweather.view.MainContract
import com.humansuit.yourweather.model.data.ErrorState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalStateException

class ForecastPresenter(
    view: ForecastView,
    private val forecastWeatherModel: ForecastWeatherModel
) : MainContract.Presenter {

    private val disposable = CompositeDisposable()
    private var view: ForecastView? = view

    override fun onViewCreated() {
        loadFiveDayForecast()
    }

    override fun onViewDetach() {
        disposable.dispose()
        view = null
    }

    private fun loadFiveDayForecast() {
        try {
            val location = forecastWeatherModel.getSavedLocation()
            disposable.add(forecastWeatherModel.getFiveDayForecast(location.first, location.second)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.doOnSubscribe { view?.showProgress(true); }
                ?.doFinally { view?.showProgress(false); view?.setEnableUi(true) }
                ?.subscribeWith(object: DisposableSingleObserver<FiveDayForecastResponse>() {
                    override fun onSuccess(response: FiveDayForecastResponse?) {
                        if (response?.forecastList != null) {
                            val forecastSectionList = forecastWeatherModel.getParsedForecast(response.forecastList)
                            view?.updateForecastList(forecastSectionList)
                        } else view?.showErrorScreen(ErrorState(ErrorList.UNDEFINED.state, R.drawable.ic_error))
                    }

                    override fun onError(e: Throwable?) {
                        if (e?.message != null) view?.showErrorScreen(ErrorState(ErrorList.NETWORK.state, R.drawable.ic_no_internet_icon))
                        else view?.showErrorScreen(ErrorState(ErrorList.NETWORK.state, R.drawable.ic_no_internet_icon))
                    }
                })
            )
        } catch(e: IllegalStateException) {
            view?.showErrorScreen(ErrorState(ErrorList.UNDEFINED.state, R.drawable.ic_error))
        }

    }

}