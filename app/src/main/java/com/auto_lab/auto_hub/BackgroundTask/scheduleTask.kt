package com.auto_lab.auto_hub.BackgroundTask

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.auto_lab.auto_hub.R
import com.auto_lab.auto_hub.data_processing.UserData
import com.auto_lab.auto_hub.notification.LectureNotificationService
import com.auto_lab.auto_hub.timetable_feature.Module
import java.time.LocalDate
import java.time.LocalTime

class DailyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val localContext = context

    fun getTimetableData(context: Context): MutableList<Module> {
        val userData = UserData()

        val dayOfWeek = LocalDate.now().dayOfWeek.toString().uppercase()
        val tempModulesArray = if(dayOfWeek =="MONDAY"){
            userData.readData(R.string.timetable_monday.toString(),context).dropLast(1)
        }else if(dayOfWeek =="TUESDAY"){
            userData.readData(R.string.timetable_tuesday.toString(),context).dropLast(1)
        }else if(dayOfWeek== "WEDNESDAY"){
            userData.readData(R.string.timetable_wednesday.toString(),context).dropLast(1)
        }else if(dayOfWeek == "THURSDAY"){
            userData.readData(R.string.timetable_thursday.toString(), context).dropLast(1)
        }else if(dayOfWeek == "FRIDAY"){
            userData.readData(R.string.timetable_friday.toString(),context).dropLast(1)
        }else{
            "weekend"
        }

        var arrayM = mutableListOf<String>()
        if(tempModulesArray.toString().isEmpty() || tempModulesArray.toString().isBlank() || tempModulesArray.toString() == "failed")
            ""
        else if(tempModulesArray.contains("weekend")){
            arrayM.add(
                "weekend;weekend;weekend;weekend"
            )
        }
        else
            arrayM = tempModulesArray.split("&&") as MutableList<String>

        //I know it hard coded values but for now it will do the trick
        val moduleArray = mutableListOf<Module>()
        if(arrayM.isNotEmpty()){
            arrayM.forEach { item ->
                if(item.contains(";")){
                    val listModule = item.split(";")
                    var moduleCode:String = listModule[0]
                    var sessionType: String = listModule[1]
                    var sessionTime: String = listModule[2]
                    var sessionVenue: String = listModule[3]
                    moduleArray.add(Module(moduleCode,sessionType,sessionTime,sessionVenue))
                }
            }
        }
        return moduleArray
    }

    override suspend fun doWork(): Result {
        // send notification
        val sendNotification = LectureNotificationService(applicationContext)
        val modules = getTimetableData(localContext)

        if (modules.isNotEmpty()) {
            if (modules[0].moduleCode != "weekend") {
                sendNotification.showInboxStyleNotification(modules)
            }
        }
        return Result.success()
    }
}