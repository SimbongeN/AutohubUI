package com.auto_lab.auto_hub

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.auto_lab.auto_hub.BackgroundTask.scheduleDailyNotification
import com.auto_lab.auto_hub.data_processing.AuthViewModel
import com.auto_lab.auto_hub.data_processing.TimeTableData
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.data_processing.update_maintenance
import com.auto_lab.auto_hub.data_processing.weatherDataModel
import com.auto_lab.auto_hub.navigationController.Navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authmodel = ViewModelProvider(this)[AuthViewModel::class.java]
        val timetableData = ViewModelProvider(this)[TimeTableData::class.java]
        val userData = ViewModelProvider(this)[UserData::class.java]
        val weatherDataModel = ViewModelProvider(this)[weatherDataModel::class.java]
        val updateMaintenance = ViewModelProvider(this)[update_maintenance::class.java]
        enableEdgeToEdge()
        setContent {
            val permissions = listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            val permissionState = rememberMultiplePermissionsState(permissions = permissions)

            LaunchedEffect(key1 = true) {
                when{
                    !permissionState.allPermissionsGranted ->{
                        permissionState.launchMultiplePermissionRequest()
                    }
                }
            }
            scheduleDailyNotification(context = LocalContext.current )
            Navigation(authmodel,timetableData,userData,weatherDataModel, updateMaintenance,LocalContext.current)
        }
    }
}



