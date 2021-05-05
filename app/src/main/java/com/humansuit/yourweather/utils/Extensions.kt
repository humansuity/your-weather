package com.humansuit.yourweather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.view.data.ErrorState


fun AppCompatActivity.setupNavView() {
    val toolbar = findViewById<Toolbar>(R.id.toolBar)
    val navView = findViewById<BottomNavigationView>(R.id.nav_view)
    val toolBarText = findViewById<TextView>(R.id.toolBarText)
    setSupportActionBar(toolbar)
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.main_app_nav_graph)
    navView.setupWithNavController(navController)
    navController.addOnDestinationChangedListener { controller, _, _ ->
        toolBarText.text = controller.currentDestination?.label
    }
}


fun Activity.showErrorScreen(error: ErrorState) {
    val bundle = Bundle().apply { putSerializable("ERROR_BUNDLE_KEY", error) }
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.error_state_nav_graph, bundle)
}


@SuppressLint("MissingPermission")
fun Activity.getLastLocation(
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


fun Activity.showProgressBar(show: Boolean) {
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
    progressBar?.visibility = if (show) View.VISIBLE else View.INVISIBLE
}


fun Activity.checkPermissions(): Boolean {
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


fun Activity.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}


@SuppressLint("MissingPermission")
fun Activity.requestNewLocationData(locationCallback: LocationCallback) {
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

fun Activity.requestPermissions() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        100
    )
}
