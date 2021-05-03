package com.humansuit.yourweather.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

fun Context.hasPermission(permission: String): Boolean {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    != PackageManager.PERMISSION_GRANTED) {

    }
    return true
}

fun Activity.requestLocation(onLocationRequested: (Location?) -> Unit) {
    val locationProvider = LocationProvider(this)
    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    locationProvider.getCurrentLocation(locationManager, object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationRequested(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER))
            locationManager.removeUpdates(this)
        }
    })

}