package com.auto_lab.auto_hub.data_processing

import androidx.lifecycle.ViewModel
import com.auto_lab.auto_hub.data_processing.api.RetrofitWeatherInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.auto_lab.auto_hub.data_processing.api.weatherModel.weatherModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class weatherDataModel: ViewModel() {
    private val weatherService = RetrofitWeatherInstance.weatherApiService
    private val _ApiResult = MutableStateFlow(false)
    val ApiResult : StateFlow<Boolean> = _ApiResult

    fun getWeatherData(city: String, callback: (weatherModel?, Boolean) -> Unit){
        val apiKey = "5285253eac4b4e24889201301241902"
        viewModelScope.launch{
            _ApiResult.value = true
            try {
                val response = weatherService.getWeatherData(apiKey,city,"no")
                delay(1000L)
                _ApiResult.value = false
                if(response.isSuccessful){
                    val weatherData : weatherModel ?= response.body()
                    //Log.i("weatherError",weatherData.toString())
                    callback(weatherData,true)
                }else{
                    //Log.i("weatherError",response.body().toString())
                    callback(null,false)
                }
            }catch (e: Exception){
               // Log.i("weatherError","error",e)
                _ApiResult.value = false
                callback(null,false)
            }
        }
    }

    fun setApiResult(state: Boolean){
        _ApiResult.value = state
    }
}