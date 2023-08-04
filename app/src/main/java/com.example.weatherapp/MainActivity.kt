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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.RectangleShape
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
                    composable("forecast/{zipCode}") { backStackEntry ->
                        ForecastScreen(backStackEntry.arguments?.getString("zipCode") ?: "55101")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WeatherInfo(navController: NavController) {
        val currentConditionsViewModel: CurrentConditionsViewModel = hiltViewModel()
        val weatherState by currentConditionsViewModel.weatherState.observeAsState()
        val safeWeatherState = weatherState ?: CurrentConditionsViewModel.WeatherState.Loading

        val zipCode = "55101"

        val isValidInput by currentConditionsViewModel.isValidInput.observeAsState(true)
        val inputZipCode by currentConditionsViewModel.inputZipCode.observeAsState("")
        val showDialog = remember { mutableStateOf(false) }

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
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.title),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 24.dp),
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = location, fontSize = 20.sp, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))

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
                        onClick = {
                            val zip = if (currentConditionsViewModel.inputZipCode.value.isNullOrBlank()) {
                                zipCode
                            } else {
                                currentConditionsViewModel.inputZipCode.value
                            }
                            navController.navigate("forecast/$zip")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .width(200.dp)
                            .height(60.dp),
                        shape = RectangleShape
                    ) {
                        Text("Forecast", fontSize = 20.sp, color = Color.White)
                    }

                    TextField(
                        value = inputZipCode,
                        onValueChange = { newValue -> currentConditionsViewModel.inputZipCode.value = newValue },
                        label = { Text("Zip Code") },
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Button(
                        onClick = {
                            currentConditionsViewModel.validateAndFetchData()
                            if (!isValidInput) {
                                showDialog.value = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(200.dp)
                            .height(60.dp),
                        shape = RectangleShape
                    ) {
                        Text("Search", fontSize = 20.sp, color = Color.White)
                    }

                    if (!isValidInput) {
                        showDialog.value = true
                    }

                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                showDialog.value = false
                            },
                            title = {
                                Text(text = "Error")
                            },
                            text = {
                                Text("Invalid Zip Code!")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDialog.value = false
                                        currentConditionsViewModel.resetValidationFlag()
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        )
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









