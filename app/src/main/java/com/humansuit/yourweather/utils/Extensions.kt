package com.humansuit.yourweather.utils

import android.app.Activity
import android.os.Bundle
import androidx.navigation.findNavController
import com.humansuit.yourweather.R
import com.humansuit.yourweather.model.data.ErrorState


fun Activity.showErrorScreen(error: ErrorState) {
    val bundle = Bundle().apply { putSerializable(KEY_BUNDLE_ERROR, error) }
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.error_state_nav_graph, bundle)
}

