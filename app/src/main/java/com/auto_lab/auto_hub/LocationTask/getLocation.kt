package com.auto_lab.auto_hub.LocationTask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object LocationHelper {

    // Check if location is enabled
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Convert Latitude & Longitude to City Name
    private fun getCityName(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.get(0)?.locality ?: "Unknown City"
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown City"
        }
    }

    // Suspend function to get location
    @SuppressLint("MissingPermission")
    suspend fun getLocation(context: Context): String {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { continuation ->
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resume("Permission Required")
                return@suspendCancellableCoroutine
            }

            fusedLocationClient.getCurrentLocation(100, null).addOnSuccessListener { location ->
                if (location != null) {
                    val cityName = getCityName(context, location.latitude, location.longitude)
                    continuation.resume(cityName)
                } else {
                    continuation.resume("Location Not Found")
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    // Open Location Settings
    fun openLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    //open Data Settings
    fun openDataSettings(context: Context){
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
        } else {
            Intent(Settings.ACTION_SETTINGS)
        }
        context.startActivity(intent)
    }
}
