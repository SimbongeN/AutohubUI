package com.auto_lab.auto_hub.BackgroundTask

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit
import com.auto_lab.auto_hub.R

fun scheduleDailyNotification(context: Context) {

    val now = Calendar.getInstance()

    //improve from this make making a work manager that checks every minu
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 7)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(now)) { // If it's already past 7:30 AM today, schedule for tomorrow
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    val delay = targetTime.timeInMillis - now.timeInMillis

    val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "DailyWorker",
        ExistingPeriodicWorkPolicy.REPLACE,
        dailyWorkRequest
    )
}