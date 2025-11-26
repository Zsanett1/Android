package com.example.activity.repository

import android.content.Context
import com.example.activity.model.ScheduleResponse
import com.example.activity.network.RetrofitClient

class ScheduleRepository (context: Context) {
    private val api by lazy { RetrofitClient.getInstance(context) }

    suspend fun getScheduleByDay(day: String): List<ScheduleResponse> {
        return  api.getScheduleByDay(day)
    }
}