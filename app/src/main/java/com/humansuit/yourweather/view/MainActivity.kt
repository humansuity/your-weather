package com.humansuit.yourweather.view

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.ActivityMainBinding
import com.humansuit.yourweather.utils.getLastLocation
import com.humansuit.yourweather.utils.saveLastLocation
import com.humansuit.yourweather.utils.setupNavView
import com.humansuit.yourweather.utils.showErrorScreen
import com.humansuit.yourweather.view.data.ErrorState

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding: ActivityMainBinding by viewBinding(R.id.container)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val loadFragmentsWithLocation: (Location) -> Unit = { location ->
        saveLastLocation(location, applicationContext)
        setupNavView()
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            loadFragmentsWithLocation(mLastLocation)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        getLastLocation(
            onSuccess = { location -> loadFragmentsWithLocation(location) },
            onFailure = { showErrorScreen(ErrorState("Location is not available, please turn it on", R.drawable.ic_location)) },
            onNewLocationRequested = mLocationCallback
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation(
                        onSuccess = { location -> loadFragmentsWithLocation(location) },
                        onFailure = { showErrorScreen(ErrorState("Location is not available, please turn it on", R.drawable.ic_location)) },
                        onNewLocationRequested = mLocationCallback
                    )
                } else showErrorScreen(ErrorState("Permissions is not granted", R.drawable.ic_permissions))
            }
        }
    }

}