package com.example.seriesfilm

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(
        @Body requset: AuthModels.LoginRequest
    ): Call<AuthModels.AuthResponse>

    @POST("register")
    fun register(
        @Body request: AuthModels.RegisterRequest
    ): Call<AuthModels.AuthResponse>
}
