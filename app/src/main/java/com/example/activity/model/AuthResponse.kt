package com.example.activity.model


data class AuthResponse(
    val tokens: Tokens,
    val user: User
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class User(
    val id: Int,
    val email: String,
    val name: String
)