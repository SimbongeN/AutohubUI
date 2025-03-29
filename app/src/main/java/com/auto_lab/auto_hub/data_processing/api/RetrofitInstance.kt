package com.auto_lab.auto_hub.data_processing.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64
import java.util.concurrent.TimeUnit

//remove android:usesCleartextTraffic="true" in the manifest .xml file
object RetrofitInstance {
    private const val baseUrl = ""

    private fun getInstance(username:String, password:String): Retrofit {
        // Encode credentials in Base64
        val credentials = "$username:$password"
        val basicAuth = "Basic ${Base64.getEncoder().encodeToString(credentials.toByteArray())}"

        // Create OkHttpClient with Basic Auth Interceptor
        val client = OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS) // Disable connection timeout
            .readTimeout(300, TimeUnit.SECONDS)    // Disable read timeout
            .writeTimeout(300, TimeUnit.SECONDS)   // Disable write timeout
            .retryOnConnectionFailure(true)
            .addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestWithAuth = originalRequest.newBuilder()
                    .header("Authorization", basicAuth) // Add the Authorization header
                    .build()
                chain.proceed(requestWithAuth)
            }
            .build()

        val gson: Gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    private fun getInstance(): Retrofit {
        // Create OkHttpClient with Basic Auth Interceptor
        val client = OkHttpClient.Builder()
            .connectTimeout(180, TimeUnit.SECONDS) // Disable connection timeout
            .readTimeout(180, TimeUnit.SECONDS)    // Disable read timeout
            .writeTimeout(180, TimeUnit.SECONDS)   // Disable write timeout
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAuth(username: String, password: String): AuthApi{
        return getInstance(username,password).create(AuthApi::class.java)
    }

    fun getTimetableApi(username: String,password: String): TimetableApi{
        return getInstance(username,password).create(TimetableApi::class.java)
    }

    val withoutAuthApi: AuthApi = getInstance().create(AuthApi::class.java)

    val withoutAuthApiTimetable =  getInstance().create(TimetableApi::class.java)

}
