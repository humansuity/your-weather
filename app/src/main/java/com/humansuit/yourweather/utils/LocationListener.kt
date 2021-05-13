package com.humansuit.yourweather.utils

import android.location.Location
import com.google.android.gms.location.LocationCallback

interface LocationListener {

    fun getLastLocation(
        onSuccess: (location: Location) -> Unit,
        onFailure: () -> Unit,
        onNewLocationRequested: LocationCallback
    )

}