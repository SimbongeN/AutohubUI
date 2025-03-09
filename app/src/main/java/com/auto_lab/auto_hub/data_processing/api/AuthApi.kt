package com.auto_lab.auto_hub.data_processing.api

import com.auto_lab.auto_hub.data_processing.api.authModel.authModel
import com.auto_lab.auto_hub.data_processing.api.authModel.recoverModel
import com.auto_lab.auto_hub.data_processing.api.authModel.registrationModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApi {
    //change the get request to a post request
    @POST("v1/users/login")
    suspend fun autheticateUser(
        @Query("email") userEmail: String
    ): Response<authModel>

    @POST("v1/users/register")
    suspend fun createUser(
        @Query("name") name: String,
        @Query("surname") surname: String,
        @Query("userEmail") email: String,
        @Query("userPassword") password: String
    ): Response<registrationModel>

    @POST("v1/verification/emailVerified")
    suspend fun checkEmailVerification(
        @Query("email") email: String
    ): Response<Boolean>

    @GET("v1/users/accountRecovery")
    suspend fun recoverAccount(
        @Query("email") email:String
    ):Response<recoverModel>

    @PUT("v1/users/updateUser")
    suspend fun updateUser(
        @Query("email") email:String,
        @Query("newPassword") password: String
    ):Response<Boolean>

    @POST("v1/users/deleteAccount")
    suspend fun deleteAccount(
        @Query("email") email: String
    ): Response<Boolean>

    @GET("v1/update/release")
    suspend fun checkNewRelease(): Response<Boolean>

    @GET("v1/update/maintaince")
    suspend fun checkMaintence(): Response<Boolean>

}