package com.humansuit.yourweather.view.error_state

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.humansuit.yourweather.R
import com.humansuit.yourweather.utils.LocationListener
import com.humansuit.yourweather.view.data.ErrorState
import java.lang.ClassCastException

class ErrorStateFragment : Fragment(R.layout.fragment_error_state) {

    private lateinit var mLocationListener: LocationListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorTextView = view.findViewById<TextView>(R.id.errorTextView)
        val icon = view.findViewById<ImageView>(R.id.iconView)
        val retryButton = view.findViewById<Button>(R.id.retryButton)
        val errorState = arguments?.getSerializable("ERROR_BUNDLE_KEY") as ErrorState

        errorTextView.text = errorState.message
        icon.setImageResource(errorState.icon)
        retryButton.setOnClickListener {
            mLocationListener.getLastLocation(
                onSuccess = { loadMainNavGraph() },
                onFailure = { showSnackbar(view) },
                onNewLocationRequested = mLocationCallback
            )
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