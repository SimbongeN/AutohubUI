package com.auto_lab.auto_hub.data_processing.api.authModel

data class authModel(
    val email: String,
    val name: String,
    val password: String,
    val surname: String,
    val verificationCode: String,
    val verified: Boolean,
    val lectureTimeTable: String,
    val assigmentTracker: String
)