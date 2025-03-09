package com.auto_lab.auto_hub.data_processing.api.weatherModel

data class Current(
    val cloud: String,
    val feelslike_c: String,
    val humidity: String,
    val is_day: String,
    val last_updated: String,
    val temp_c: String,
    val wind_mph: String
)