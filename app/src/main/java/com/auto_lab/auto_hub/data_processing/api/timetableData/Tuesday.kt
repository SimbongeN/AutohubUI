package com.auto_lab.auto_hub.data_processing.api.timetableData

data class Tuesday(
    val Lecture: List<Lecture>,
    val Practical: List<Practical>,
    val Tutorial: List<Tutorial>
)