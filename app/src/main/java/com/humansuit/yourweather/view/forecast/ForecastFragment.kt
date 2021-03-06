package com.humansuit.yourweather.view.forecast

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.FragmentForecastBinding
import com.humansuit.yourweather.model.ForecastWeatherModel
import com.humansuit.yourweather.model.data.ErrorState
import com.humansuit.yourweather.model.data.ForecastSection
import com.humansuit.yourweather.utils.ActivityStateObserver
import com.humansuit.yourweather.utils.showErrorScreen
import com.humansuit.yourweather.view.MainContract
import com.humansuit.yourweather.view.adapter.ForecastListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast), ForecastView {

    private val viewBinding: FragmentForecastBinding by viewBinding()
    private var presenter: MainContract.Presenter? = null

    @Inject lateinit var forecastWeatherModel: ForecastWeatherModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPresenter(ForecastPresenter(this, forecastWeatherModel))
        presenter?.onViewCreated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(ActivityStateObserver(
            onCreateStateCallback = { setEnableUi(enable = false) })
        )
    }

    override fun onDetach() {
        presenter?.onViewDetach()
        super.onDetach()
    }

    override fun setEnableUi(enable: Boolean) {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.menu.forEach { it.isEnabled = enable }
    }

    override fun updateForecastList(forecastList: List<ForecastSection>) {
        with(viewBinding) {
            recyclerView.adapter = ForecastListAdapter(forecastSectionList = forecastList)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showProgress(show: Boolean) {
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if (show) View.VISIBLE else View.INVISIBLE

    }

    override fun showErrorScreen(error: ErrorState) {
        requireActivity().showErrorScreen(error)
    }

}