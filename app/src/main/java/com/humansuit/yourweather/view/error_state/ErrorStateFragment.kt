package com.humansuit.yourweather.view.error_state

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.FragmentErrorStateBinding
import com.humansuit.yourweather.model.data.ErrorState
import com.humansuit.yourweather.utils.KEY_BUNDLE_ERROR
import com.humansuit.yourweather.utils.LocationListener

class ErrorStateFragment : Fragment(R.layout.fragment_error_state) {

    private val viewBinding: FragmentErrorStateBinding by viewBinding()
    private lateinit var mLocationListener: LocationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorState = arguments?.getSerializable(KEY_BUNDLE_ERROR) as ErrorState

        with(viewBinding) {
            errorTextView.text = errorState.message
            iconView.setImageResource(errorState.icon)
            retryButton.setOnClickListener {
                retryButton.isEnabled = false
                mLocationListener.getLastLocation(
                    onSuccess = { loadMainNavGraph() },
                    onFailure = {
                        showSnackbar(view)
                        retryButton.isEnabled = true
                    },
                    onNewLocationRequested = mLocationCallback
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { mLocationListener = context as LocationListener }
        catch (e: ClassCastException) {  }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            loadMainNavGraph()
        }
    }

    private fun loadMainNavGraph() {
        findNavController().setGraph(R.navigation.main_app_nav_graph)
    }

    private fun showSnackbar(view: View) {
        Snackbar.make(view, "Can't get your location!", Snackbar.LENGTH_SHORT).show()
    }

}