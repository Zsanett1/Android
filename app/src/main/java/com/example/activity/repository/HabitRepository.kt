package com.example.activity.repository

import android.content.Context
import com.example.activity.model.HabitCategory
import com.example.activity.model.HabitCreateRequest
import com.example.activity.model.HabitResponse
import com.example.activity.network.RetrofitClient

class HabitRepository(context: Context) {

    private val api = RetrofitClient.getInstance(context)

    suspend fun getCategories(): List<HabitCategory> {
        return api.getHabitCategories()
    }

    suspend fun createHabit(
        name: String,
        description: String?,
        goal: String,
        categoryId: Long
    ): HabitResponse {
        val request = HabitCreateRequest(
            name = name,
            description = description,
            goal = goal,
            categoryId = categoryId
        )
        return api.createHabit(request)
    }
}
