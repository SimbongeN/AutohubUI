package com.auto_lab.auto_hub.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class NotificationModel : Application(){

    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            "lecture_reminder",
            "lecture Reminder channel",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}