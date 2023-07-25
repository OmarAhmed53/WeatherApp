package com.example.weatherapp

import retrofit2.http.GET

interface ApiService {

    @GET("/path/to/data")
    suspend fun getWeatherData(): WeatherData

    @GET("forecast/daily")
    suspend fun getForecastData(): Forecast

}