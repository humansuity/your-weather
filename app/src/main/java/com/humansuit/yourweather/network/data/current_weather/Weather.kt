package com.humansuit.yourweather.network.data.current_weather

data class Weather (
    val id : Int,
    val main : String,
    val description : String,
    val icon : String
)
