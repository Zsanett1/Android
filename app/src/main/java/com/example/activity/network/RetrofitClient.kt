package com.example.activity.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.activity.network.ApiService
import com.example.activity.utils.LocalDateTimeAdapter
import com.example.activity.utils.SessionManager
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.LocalDateTime

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"
    fun getInstance(context: Context): ApiService {

        // 🔹 1. Logging interceptor (kiírja a kéréseket)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        // 🔹 2. Token interceptor (Authorization header)
        val tokenInterceptor = Interceptor { chain ->
            val token = SessionManager(context).fetchAuthToken()
            val request = if (token != null) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }

        // 🔹 3. Build OkHttp client ezekkel az interceptorokkal
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(context.applicationContext))
            .build()

        val gson = GsonBuilder().registerTypeAdapter(
            LocalDateTime::class.java,
                LocalDateTimeAdapter()
        ).create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
