package com.auto_lab.auto_hub.data_processing.api

import com.auto_lab.auto_hub.data_processing.api.weatherModel.weatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherApi {
    @GET("v1/current.json")
    suspend fun getWeatherData(
        @Query("key") key:String,
        @Query("q") location: String,
        @Query("aqi") ariQuality: String
    ): Response<weatherModel>
}