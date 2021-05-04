package com.humansuit.yourweather.utils


import android.app.Activity
import android.util.Log


fun Activity.showErrorScreen(error: String) {
    Log.e("Error", error)
}