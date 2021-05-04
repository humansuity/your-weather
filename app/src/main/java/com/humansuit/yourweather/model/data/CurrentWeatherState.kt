package com.humansuit.yourweather.model.data

data class CurrentWeatherState(
    val location: String,
    val weatherState: String,
    val humidity: String,
    val pressure: String,
    val temperature: String,
    val windSpeed: String,
    val windDirection: String,
    val rainfall: String
) {
    override fun toString(): String {
        return "Current weather by $location:\n" +
                "Weather state: $weatherState\n" +
                "Temperature: $temperature\n" +
                "Humidity: $humidity\n" +
                "Rainfall: $rainfall\n" +
                "Wind speed: $windSpeed\n" +
                "Wind direction: $windDirection"
    }
}
