package com.auto_lab.auto_hub.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.auto_lab.auto_hub.timetable_feature.Module
import kotlin.random.Random

class LectureNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showInboxStyleNotification(ListModules : MutableList<Module>) {
        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle("Today's Lectures")

        //fix module time
        var moduleTime = ""

        // Add each module to the notification
        for (module in ListModules) {
            if(module.sessionTime.split("-").size > 2){
               moduleTime = "${module.sessionTime.split("-")[0]}-${module.sessionTime.split("-")[module.sessionTime.split("-").size-1]}"
            }else{
                moduleTime = module.sessionTime
            }
            if(module.moduleCode != "nA"){
                inboxStyle.addLine("$moduleTime - ${module.moduleCode}")
            }
        }

        val notification = NotificationCompat.Builder(context, "lecture_reminder")
            .setContentTitle("Lecture Reminder")
            .setContentText("Your today's lectures")
            .setSmallIcon(com.auto_lab.auto_hub.R.drawable.notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(inboxStyle)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

}