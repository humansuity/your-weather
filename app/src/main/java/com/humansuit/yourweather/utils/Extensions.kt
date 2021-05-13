package com.humansuit.yourweather.utils

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.humansuit.yourweather.R
import com.humansuit.yourweather.view.data.ErrorState


fun AppCompatActivity.setupNavView() {
    val toolbar = findViewById<Toolbar>(R.id.toolBar)
    val navView = findViewById<BottomNavigationView>(R.id.nav_view)
    val toolBarText = findViewById<TextView>(R.id.toolBarText)
    setSupportActionBar(toolbar)
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.main_app_nav_graph)
    navView.setupWithNavController(navController)
    navController.addOnDestinationChangedListener { controller, _, _ ->
        toolBarText.text = controller.currentDestination?.label
    }
}


fun Activity.showErrorScreen(error: ErrorState) {
    val bundle = Bundle().apply { putSerializable("ERROR_BUNDLE_KEY", error) }
    val navController = findNavController(R.id.nav_host_fragment)
    navController.setGraph(R.navigation.error_state_nav_graph, bundle)
}

