package com.example.weatherapp

import android.os.Bundle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberImagePainter
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.material3.ButtonDefaults
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "weather") {
                    composable("weather") { WeatherInfo(navController) }
                    composable("forecast") { ForecastScreen() }
                }
            }
        }
    }

    @Composable
    fun WeatherInfo(navController: NavController) {
        val currentConditionsViewModel: CurrentConditionsViewModel = hiltViewModel()
        val weatherState by currentConditionsViewModel.weatherState.observeAsState()
        val safeWeatherState = weatherState ?: CurrentConditionsViewModel.WeatherState.Loading

        // Define your actual coordinates
        val zipCode = "55101"

        // Trigger the getWeatherData() function
        LaunchedEffect(currentConditionsViewModel) {
            currentConditionsViewModel.getCurrentData(zipCode)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (safeWeatherState) {
                is CurrentConditionsViewModel.WeatherState.Loading -> {
                    Text(text = "Loading...", fontSize = 20.sp, textAlign = TextAlign.Center)
                }

                is CurrentConditionsViewModel.WeatherState.Success -> {
                    val weatherData = (weatherState as CurrentConditionsViewModel.WeatherState.Success).currentData
                    val location = "${weatherData.cityName}, ${weatherData.sys?.country}"
                    val currentForecast = weatherData.mainForecast ?: return
                    val temperature = "${currentForecast.temp}\u00B0"
                    val feelsLike = "Feels like ${currentForecast.feelsLike}\u00B0"
                    val lowTemp = "Low ${currentForecast.temp_min}\u00B0"
                    val highTemp = "High ${currentForecast.temp_max}\u00B0"
                    val humidity = "Humidity ${currentForecast.humidity}%"
                    val pressure = "Pressure ${currentForecast.pressure} hPA"
                    val weatherIcon = weatherData.iconUrl


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.CenterStart // Aligns the title to the left
                    ) {
                        Text(
                            text = stringResource(id = R.string.title),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 24.dp),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))  // Reduced space

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = location, fontSize = 20.sp, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))  // Reduced space

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(50.dp)
                        ) {
                            Text(text = temperature, fontSize = 48.sp)
                            Text(text = feelsLike, fontSize = 14.sp)
                        }

                        Image(
                            painter = rememberImagePainter(weatherIcon),
                            contentDescription = stringResource(id = R.string.weather_icon),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .size(150.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))  // Reduced space

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(0.2f)
                                .padding(25.dp)
                        ) {
                            Text(text = lowTemp, fontSize = 20.sp)
                            Text(text = highTemp, fontSize = 20.sp)
                            Text(text = humidity, fontSize = 20.sp)
                            Text(text = pressure, fontSize = 20.sp)
                        }
                    }

                    Button(
                        onClick = { navController.navigate("forecast") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Forecast", fontSize = 20.sp, color = Color.White)
                    }
                }

                is CurrentConditionsViewModel.WeatherState.Error -> {
                    val error = (safeWeatherState).error
                    Text(text = "Error: $error", fontSize = 20.sp, color = Color.Red)
                }
            }
        }
    }
}









