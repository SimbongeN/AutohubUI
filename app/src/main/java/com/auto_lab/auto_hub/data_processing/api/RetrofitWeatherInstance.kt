package com.auto_lab.auto_hub.data_processing.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitWeatherInstance {
    private const val baseUrl = "https://api.weatherapi.com/"

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApiService: weatherApi  = getInstance().create(weatherApi::class.java)
}