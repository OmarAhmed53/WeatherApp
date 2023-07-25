package com.example.weatherapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val viewModel: WeatherViewModel by viewModel()
    val forecastData by viewModel.forecastData.observeAsState()
    val forecastItems = forecastData?.forecasts ?: emptyList<ForecastItem>()

    LaunchedEffect(key1 = true) {
        viewModel.getForecastData() // Correct method to call
    }

    Column {
        // Title bar
        Box(modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.Gray)) {
            Text("Forecast", modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp), fontSize = 24.sp)
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
fun ForecastRow(forecast: ForecastItem) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Weather icon
        Image(
            painter = rememberImagePainter(forecast.iconUrl),
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
                text = "Sunrise: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(forecast.sunriseTime)}",
                fontSize = 16.sp
            )
            Text(
                text = "Sunset: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(forecast.sunsetTime)}",
                fontSize = 16.sp
            )
        }
    }
}



