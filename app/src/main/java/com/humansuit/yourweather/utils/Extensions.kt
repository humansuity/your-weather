package com.humansuit.yourweather.utils

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.view.data.ErrorState


fun Activity.showErrorScreen(error: ErrorState) {
    val bundle = Bundle().apply { putSerializable("ERROR_BUNDLE_KEY", error) }
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.error_state_nav_graph, bundle)
}


fun Activity.showProgressBar(show: Boolean) {
    val progressBar = findViewById<ProgressBar>(R.id.progressBar)
    val navView = findViewById<BottomNavigationView>(R.id.nav_view)
    progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    navView.menu.forEach { it.isEnabled = !show }
}

