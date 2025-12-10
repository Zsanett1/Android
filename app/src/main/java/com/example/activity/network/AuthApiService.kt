package com.example.activity.network

import com.example.activity.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    // Date in YYYY-MM-DD format
    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String): List<ScheduleResponse>

    @GET("/habit/categories")
    suspend fun getHabitCategories(): List<HabitCategory>

    @POST("/habit")
    suspend fun createHabit(@Body request: HabitCreateRequest): HabitResponse

    @GET("/habit")
    suspend fun getHabits(): List<HabitResponse>

    @POST("/schedule/custom")
    suspend fun createCustomSchedule(@Body request: CreateScheduleRequest): ScheduleResponse

}
