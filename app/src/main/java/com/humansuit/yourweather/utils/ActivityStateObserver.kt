package com.humansuit.yourweather.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityStateObserver(
    private val onCreateStateCallback: () -> Unit
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.removeObserver(this)
        onCreateStateCallback()
    }
}