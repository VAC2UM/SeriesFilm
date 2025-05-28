package com.example.seriesfilm

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(
        @Body requset: AuthModels.LoginRequest,
    ): Call<AuthModels.AuthResponse>

    @POST("register")
    fun register(
        @Body request: AuthModels.RegisterRequest,
    ): Call<AuthModels.AuthResponse>

    @POST("favorites/add")
    fun addToFavorites(
        @Header("Authorization") token: String,
        @Body request: AuthModels.FavoriteRequest,
    ): Call<ResponseBody>
}
