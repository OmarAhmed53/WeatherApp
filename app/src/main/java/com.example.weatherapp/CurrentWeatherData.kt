package com.example.weatherapp

import com.squareup.moshi.Json

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class CurrentWeatherData(
    @Json(name = "name")
    val cityName: String?,
    @Json(name = "sys")
    val sys: Sys?,
    @Json(name = "main")
    val mainForecast: MainForecastItem,
    val weather: List<Weather>
) {
    val iconUrl: String
        get() {
            return "https://openweathermap.org/img/wn/${weather.firstOrNull()?.icon}@2x.png"
        }
}

data class Sys(
    val country: String?
)

data class MainForecastItem(
    val temp: Double?,
    @Json(name = "feels_like")
    val feelsLike: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Double?,
    val humidity: Double?,
)