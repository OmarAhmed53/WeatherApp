package com.example.weatherapp

import com.squareup.moshi.Json
import java.util.Date

data class Temperature(
    val date: Double,
    val low: Double,
    val high: Double
)

data class WeatherCondition(
    val icon: String,
)

data class WeatherData(
    val conditionDescription: String,
    private val temperature: Temperature,
    @Json(name= "weather")
    private val weatherConditions: List<WeatherCondition>,
) {
    val highTemp: Double
        get() = temperature.high

    val lowTemp: Double
        get() = temperature.low

    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/${weatherConditions.firstOrNull()?.icon}@2x.png"
}

data class ForecastItem(
    @Json(name = "weather")
    val weatherConditions: List<WeatherCondition>,
    val dt: Long, // Assuming the date is given as a Unix timestamp
    val temp: Temperature, // Assuming the temperatures are given in a "temp" object
    val sunrise: Long, // Assuming the sunrise time is given as a Unix timestamp
    val sunset: Long // Assuming the sunset time is given as a Unix timestamp
) {
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/${weatherConditions.firstOrNull()?.icon}@2x.png"

    // Convert Unix timestamps to Date objects for convenience
    val date: Date
        get() = Date(dt * 1000L)
    val sunriseTime: Date
        get() = Date(sunrise * 1000L)
    val sunsetTime: Date
        get() = Date(sunset * 1000L)
}

data class Forecast(

    @Json(name = "list")
    val forecasts: List<ForecastItem>
)