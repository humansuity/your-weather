package com.humansuit.yourweather.view

import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.humansuit.yourweather.R
import com.humansuit.yourweather.utils.getLastLocation
import com.humansuit.yourweather.utils.setupNavView
import com.humansuit.yourweather.view.data.ErrorState

class ErrorStateFragment : Fragment(R.layout.fragment_error_state) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorTextView = view.findViewById<TextView>(R.id.errorTextView)
        val icon = view.findViewById<ImageView>(R.id.iconView)
        val errorState = arguments?.getSerializable("ERROR_BUNDLE_KEY") as ErrorState
        errorTextView.text = errorState.message
        icon.setImageResource(errorState.icon)

        val retryButton = view.findViewById<Button>(R.id.retryButton)
        retryButton.setOnClickListener {
            requireActivity().getLastLocation(
                onSuccess = { loadMainNavGraph() },
                onFailure = { showSnackbar(view) },
                onNewLocationRequested = mLocationCallback
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            loadMainNavGraph()
        }
    }

    private fun loadMainNavGraph() {
        findNavController().setGraph(R.navigation.main_app_nav_graph)
        (requireActivity() as AppCompatActivity).setupNavView()
    }

    private fun showSnackbar(view: View) {
        Snackbar.make(view, "Can't get your location!", Snackbar.LENGTH_SHORT).show()
    }

}