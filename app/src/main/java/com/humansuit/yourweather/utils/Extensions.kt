package com.humansuit.yourweather.utils

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.forEach
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.model.data.ErrorState


fun Activity.showErrorScreen(error: ErrorState) {
    val bundle = Bundle().apply { putSerializable(KEY_BUNDLE_ERROR, error) }
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.error_state_nav_graph, bundle)
}

