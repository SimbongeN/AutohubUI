package com.auto_lab.auto_hub.data_processing.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URLEncoder

suspend fun loginRequest(username: String, password: String): Response{
    val client = OkHttpClient()

    // Generate Basic Auth credentials
    val credentials = Credentials.basic(username, password)

    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val request = Request.Builder()
        .url("https://e864-41-144-1-117.ngrok-free.app/v1/users/login?email=$encodedUsername")
        .header("Authorization", credentials)
        .build()

    return withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        Log.i("API Response", response.toString())
        response
    }
}
