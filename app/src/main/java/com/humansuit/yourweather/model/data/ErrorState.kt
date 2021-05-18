package com.humansuit.yourweather.model.data

import java.io.Serializable

data class ErrorState(
    val message: String,
    val icon: Int
) : Serializable