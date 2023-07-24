package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import java.text.SimpleDateFormat
import java.util.*

// Data classes to hold forecast information
data class DayForecast(
    val date: Date,
    val weatherIcon: String,
    val temp: ForecastTemp,
    val sunrise: Date,
    val sunset: Date
)

data class ForecastTemp(
    val high: Int,
    val low: Int
)

@Composable
fun ForecastScreen() {
    Column {
        // Title bar
        Box(modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.Gray)) {
            Text("Forecast", modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp), fontSize = 24.sp)
        }

        // Mock data for forecast
        val weatherIcons = listOf("01d", "01n", "02d", "02n", "03d", "03n", "04d", "04n", "09d", "09n", "10d", "10n", "11d", "11n", "13d", "13n") // various weather conditions
        val forecastItems = List(16) { index ->
            DayForecast(
                date = Date(System.currentTimeMillis() + index * 24 * 60 * 60 * 1000L),
                weatherIcon = "https://openweathermap.org/img/wn/${weatherIcons[index]}@2x.png",
                temp = ForecastTemp(high = 75 + index, low = 55 + index),
                sunrise = Date(System.currentTimeMillis() + index * 24 * 60 * 60 * 1000L),
                sunset = Date(System.currentTimeMillis() + index * 24 * 60 * 60 * 1000L)
            )
        }

        // Display forecast items in a LazyColumn
        LazyColumn {
            items(forecastItems) { forecastItem ->
                ForecastRow(forecastItem)
            }
        }
    }
}

@Composable
fun ForecastRow(forecast: DayForecast) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Weather icon
        Image(
            painter = rememberImagePainter(forecast.weatherIcon),
            contentDescription = stringResource(id = R.string.weather_icon),
            modifier = Modifier.size(50.dp)
        )
        // Date
        Text(
            text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(forecast.date),
            fontSize = 20.sp
        )
        // Temperature (High and Low)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "High: ${forecast.temp.high}°", fontSize = 16.sp)
            Text(text = "Low: ${forecast.temp.low}°", fontSize = 16.sp)
        }
        // Sunrise and Sunset
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sunrise: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(forecast.sunrise)}",
                fontSize = 16.sp
            )
            Text(
                text = "Sunset: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(forecast.sunset)}",
                fontSize = 16.sp
            )
        }
    }
}

