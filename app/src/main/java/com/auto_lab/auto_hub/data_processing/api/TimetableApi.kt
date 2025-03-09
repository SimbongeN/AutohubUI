package com.auto_lab.auto_hub.data_processing.api

import com.auto_lab.auto_hub.data_processing.api.timetableData.campusModules
import com.auto_lab.auto_hub.data_processing.api.timetableData.examData
import com.auto_lab.auto_hub.data_processing.api.timetableData.urlModel
import com.auto_lab.auto_hub.data_processing.api.timetableData.userDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimetableApi {

    //get all campus modules
    @GET("v1/service/timetable/allModules")
    suspend fun getCampusModules(
        @Query("campus") campus:String
    ): Response<campusModules>

    //get user module urls
    @GET("v1/service/timetable/moduleURL")
    suspend fun getUserModuleUrls(
        @Query("modules") modules: String,
        @Query("campus") campus: String
    ): Response<urlModel>

    //get user timetable details
    @GET("v1/service/timetable/creation")
    suspend fun getTimetableDetails(
        @Query("userData") userUrls: String
    ): Response<userDataModel>

    //this method is not yet implemented but should be implemented by end of Feb June exam timetable are currently not realsed hence this simple trick but full method should be done by feb
    @GET("v1/exam/timetable/examInfo")
    suspend fun getExamDetails(
        @Query("modules") moduleInfor: String,
        @Query("campus") campus: String
    ): Response<examData>

    @POST("v1/users/LectureTimeTable")
    suspend fun saveLectureTimeTable(
        @Query("email") userEmail: String,
        @Query("LectureTimeTable") timetabledata: String
    ):Response<String>
}