package com.humansuit.yourweather.utils

enum class ErrorList(val state: String) {

    LOCATION("Location is not available, please turn it on"),
    PERMISSION("Permissions is not granted"),
    NETWORK("Internet connection problem, check whether you connected to the network"),
    UNDEFINED("Something went wrong")

}