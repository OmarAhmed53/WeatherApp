package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentConditionsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    sealed class WeatherState {
        object Loading : WeatherState()
        data class Success(val currentData: CurrentWeatherData) : WeatherState()
        data class Error(val error: String) : WeatherState()
    }

    val inputZipCode = MutableLiveData("")
    val isValidInput = MutableLiveData(true)

    private val _weatherState = MutableLiveData<WeatherState>(WeatherState.Loading)
    val weatherState: LiveData<WeatherState> get() = _weatherState

    fun getCurrentData(zipCode: String) = viewModelScope.launch {
        _weatherState.value = WeatherState.Loading
        try {
            val weatherData = apiService.getCurrentData(zipCode)
            _weatherState.value = weatherData?.let { WeatherState.Success(it) } ?: WeatherState.Error("An unknown error occurred")
        } catch (e: Exception) {
            _weatherState.value = WeatherState.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun validateAndFetchData() {
        if (inputZipCode.value?.length == 5 && inputZipCode.value?.all { it.isDigit() } == true) {
            getCurrentData(inputZipCode.value!!)
            isValidInput.value = true
        } else {
            isValidInput.value = false
        }
    }

    fun resetValidationFlag() {
        isValidInput.value = true
    }
}
