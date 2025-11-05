package com.example.activity.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.activity.model.AuthRequest
import com.example.activity.model.AuthResponse

interface ApiService {

    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}
