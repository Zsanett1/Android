package com.example.activity.repository

import android.content.Context
import com.example.activity.model.AuthRequest
import com.example.activity.network.RetrofitClient


class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)
    suspend fun login(email: String, password: String) =
        api.login(AuthRequest(email = email, password = password))
}

