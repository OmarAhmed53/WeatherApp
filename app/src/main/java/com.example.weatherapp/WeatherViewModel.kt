//package com.example.weatherapp
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class WeatherViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {
//
//    private val _weatherData: MutableLiveData<WeatherData> = MutableLiveData()
//    val weatherData: LiveData<WeatherData>
//        get() = _weatherData
//
//    private val _forecastData: MutableLiveData<Forecast> = MutableLiveData()
//    val forecastData: LiveData<Forecast>
//        get() = _forecastData
//
//    fun viewAppeared() = viewModelScope.launch {
//        _weatherData.value = apiService.getWeatherData()
//    }
//
//    fun getForecastData() = viewModelScope.launch {
//        _forecastData.value = apiService.getForecastData()
//    }
//}
package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val apiService: ApiService): ViewModel() {

    // WeatherState sealed class to represent different states of the WeatherData
    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val weatherData: WeatherData) : WeatherState()
        data class Error(val error: String) : WeatherState()
    }

    private val _weatherData: MutableLiveData<WeatherData> = MutableLiveData()
    val weatherData: LiveData<WeatherData> get() = _weatherData

    private val _forecastData: MutableLiveData<Forecast> = MutableLiveData()
    val forecastData: LiveData<Forecast> get() = _forecastData

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> get() = _weatherState

    fun viewAppeared() = viewModelScope.launch {
        _weatherState.value = WeatherState.Loading
        try {
            _weatherData.value = apiService.getWeatherData()
            _weatherState.value = WeatherState.Success(_weatherData.value!!)
        } catch (e: Exception) {
            _weatherState.value = WeatherState.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun getForecastData() = viewModelScope.launch {
        _weatherState.value = WeatherState.Loading
        try {
            _forecastData.value = apiService.getForecastData()
            _weatherState.value = WeatherState.Success(_weatherData.value!!)
        } catch (e: Exception) {
            _weatherState.value = WeatherState.Error(e.message ?: "An unknown error occurred")
        }
    }
}

