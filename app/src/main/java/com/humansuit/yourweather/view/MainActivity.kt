package com.humansuit.yourweather.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.humansuit.yourweather.R
import com.humansuit.yourweather.utils.*
import com.humansuit.yourweather.utils.LocationListener
import com.humansuit.yourweather.view.data.ErrorState

class MainActivity : AppCompatActivity(R.layout.activity_main), LocationListener {

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


    @SuppressLint("MissingPermission")
    override fun getLastLocation(
        onSuccess: (location: Location) -> Unit,
        onFailure: () -> Unit,
        onNewLocationRequested: LocationCallback
    ) {
        showProgressBar(show = true)
        if (checkPermissions()) {
            if (isLocationEnabled())
                LocationServices.getFusedLocationProviderClient(applicationContext)
                    .lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        if (location == null) requestNewLocationData(onNewLocationRequested)
                        else {
                            onSuccess(location)
                            showProgressBar(show = false)
                        }
                    }
            else {
                onFailure()
                showProgressBar(show = false)
            }
        } else {
            showProgressBar(show = false)
            requestPermissions()
        }
    }


    private fun showProgressBar(show: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) return true
        return false
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            100
        )
    }


    @SuppressLint("MissingPermission")
    fun requestNewLocationData(locationCallback: LocationCallback) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(
                mLocationRequest, locationCallback,
                Looper.myLooper()
            )
    }

}