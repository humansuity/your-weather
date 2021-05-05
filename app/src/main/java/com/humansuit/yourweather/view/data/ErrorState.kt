package com.humansuit.yourweather.view.data

import java.io.Serializable

data class ErrorState(
    val message: String,
    val icon: Int
) : Serializable