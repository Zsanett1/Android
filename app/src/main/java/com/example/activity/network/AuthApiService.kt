package com.example.activity.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.activity.model.AuthRequest
import com.example.activity.model.AuthResponse
import com.example.activity.model.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    // Date in YYYY-MM-DD format
    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String): List<ScheduleResponse>
}
