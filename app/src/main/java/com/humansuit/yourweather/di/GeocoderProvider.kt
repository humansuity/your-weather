package com.humansuit.yourweather.di

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class GeocoderProvider @Inject constructor(@ApplicationContext context: Context) {
    val geocoder = Geocoder(context, Locale.getDefault())
}