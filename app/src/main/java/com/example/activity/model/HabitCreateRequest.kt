package com.example.activity.model

data class HabitCreateRequest (
    val name: String,
    val description: String?,
    val goal: String,
    val categoryId: Long
)