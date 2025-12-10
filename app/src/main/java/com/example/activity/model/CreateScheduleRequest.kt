package com.example.activity.model

data class CreateScheduleRequest(
    val habitId: Long,
    val date: String,
    val start_time: String,
    val end_time: String,
    val is_custom: Boolean,
    val notes: String?
)