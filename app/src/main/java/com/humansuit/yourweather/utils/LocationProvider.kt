package com.humansuit.yourweather.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.app.ActivityCompat

class LocationProvider(private val context: Context) {

    fun getCurrentLocation(locationManager: LocationManager, locationListener: LocationListener) {
        val isGpsAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsAvailable) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        ActivityCompat.requestPermissions(context as AppCompatActivity, permissions, 100)
                return
            }
            Log.e("Location", "Getting current location...")
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, 0F,
                    locationListener)
        } else {
            Log.e("Location", "Gps is not available")
        }
    }

}