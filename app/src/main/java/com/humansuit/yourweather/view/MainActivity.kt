package com.humansuit.yourweather.view

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.ActivityMainBinding
import com.humansuit.yourweather.utils.showErrorScreen

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding: ActivityMainBinding by viewBinding(R.id.container)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    private val loadFragmentsWithLocation: (Location) -> Unit = { location ->
        saveLastLocation(location)
        setupNavView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navView = findViewById(R.id.nav_view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        getLastLocation(
                onSuccess = { location -> loadFragmentsWithLocation(location) },
                onFailure = { showErrorScreen("Location is not available, please turn it on") }
        )
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            100 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation(
                            onSuccess = { location -> loadFragmentsWithLocation(location) },
                            onFailure = { showErrorScreen("Location is not available, please turn it on") }
                    )
                } else showErrorScreen("Permissions is not granted")
            }
        }
    }


    private fun showProgressBar(show: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }


    private fun saveLastLocation(location: Location) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putString("latitude", location.latitude.toString())
        editor.putString("longitude", location.longitude.toString())
        editor.commit()
    }


    private fun setupNavView() {
        setSupportActionBar(viewBinding.toolBar)
        navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.mobile_navigation)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, _, _ ->
            viewBinding.toolBarText.text = controller.currentDestination?.label
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation(onSuccess: (location: Location) -> Unit, onFailure: () -> Unit) {
        showProgressBar(show = true)
        if (checkPermissions()) {
            if (isLocationEnabled())
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) requestNewLocationData()
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

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            loadFragmentsWithLocation(mLastLocation)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ),
                100
        )
    }


}