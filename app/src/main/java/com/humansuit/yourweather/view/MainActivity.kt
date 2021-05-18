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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.humansuit.yourweather.R
import com.humansuit.yourweather.databinding.ActivityMainBinding
import com.humansuit.yourweather.model.data.ErrorState
import com.humansuit.yourweather.utils.ErrorList
import com.humansuit.yourweather.utils.LocationListener
import com.humansuit.yourweather.utils.saveLastLocation
import com.humansuit.yourweather.utils.showErrorScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), LocationListener {

    private val viewBinding by viewBinding(ActivityMainBinding::bind, R.id.container)

    private val loadFragmentsWithLocation: (Location) -> Unit = { location ->
        saveLastLocation(location, applicationContext)
        setupMainNavGraph()
        initUiComponents()
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
            onFailure = { showErrorScreen(
                ErrorState(ErrorList.LOCATION.state, R.drawable.ic_location)
            ) },
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
                        onFailure = { showErrorScreen(
                            ErrorState(ErrorList.LOCATION.state, R.drawable.ic_location)
                        ) },
                        onNewLocationRequested = mLocationCallback
                    )
                } else showErrorScreen(
                    ErrorState(ErrorList.PERMISSION.state, R.drawable.ic_permissions)
                )
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
        setEnableUi(enable = false)
        if (checkPermissions()) {
            if (isLocationEnabled())
                LocationServices.getFusedLocationProviderClient(applicationContext)
                    .lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        if (location != null) {
                            onSuccess(location)
                            showProgressBar(show = false)
                            setEnableUi(enable = true)
                        }
                        else requestNewLocationData(onNewLocationRequested)
                    }
            else {
                onFailure()
                showProgressBar(show = false)
                setEnableUi(enable = true)
            }
        } else {
            showProgressBar(show = false)
            setEnableUi(enable = true)
            requestPermissions()
        }
    }


    private fun setEnableUi(enable: Boolean) {
        viewBinding.navView.menu.forEach { it.isEnabled = enable }
    }

    private fun initUiComponents() {
        val navController = findNavController(R.id.nav_host_fragment)
        setSupportActionBar(viewBinding.toolBar)
        viewBinding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, _, _ ->
            viewBinding.toolBarText.text = controller.currentDestination?.label
        }
    }


    private fun setupMainNavGraph() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.main_app_nav_graph)
    }


    private fun showProgressBar(show: Boolean) {
        viewBinding.progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
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
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
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