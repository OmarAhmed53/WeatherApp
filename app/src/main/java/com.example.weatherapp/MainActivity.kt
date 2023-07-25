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
import androidx.compose.runtime.livedata.collectAsState
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                val weatherViewModel: WeatherViewModel = hiltViewModel()
                NavHost(navController = navController, startDestination = "weather") {
                    composable("weather") { WeatherInfo(navController, weatherViewModel) }
                    composable("forecast") { ForecastScreen() }
                }
            }
        }
    }

    @Composable
    fun WeatherInfo(navController: NavController, weatherViewModel: WeatherViewModel) {
        val weatherState by weatherViewModel.weatherState.collectAsState()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (weatherState) {
                is WeatherViewModel.WeatherState.Loading -> {
                    Text(text = "Loading...", fontSize = 20.sp, textAlign = TextAlign.Center)
                }

                is WeatherViewModel.WeatherState.Success -> {
                    val weatherData =
                        (weatherState as WeatherViewModel.WeatherState.Success).weatherData
                    val location = weatherData.location
                    val temperature = "${weatherData.highTemp}\u00B0"
                    val feelsLike = "Feels like ${weatherData.feelsLike}\u00B0"
                    val lowTemp = "Low ${weatherData.lowTemp}\u00B0"
                    val highTemp = "High ${weatherData.highTemp}\u00B0"
                    val humidity = "Humidity ${weatherData.humidity}%"
                    val pressure = "Pressure ${weatherData.pressure} hPA"
                    val weatherIcon =
                        weatherData.iconUrl // This should be a valid URL to a weather icon image

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
                            Text(text = temperature, fontSize = 72.sp)
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

                is WeatherViewModel.WeatherState.Error -> {
                    val error = (weatherState as WeatherViewModel.WeatherState.Error).message
                    Text(text = "Error: $error", fontSize = 20.sp, color = Color.Red)
                }
            }
        }
    }
}









