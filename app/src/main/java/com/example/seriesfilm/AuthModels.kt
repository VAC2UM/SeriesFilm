package com.example.seriesfilm

class AuthModels {
    data class LoginRequest(
        val login: String,
        val password: String,
    )

    data class RegisterRequest(
        val login: String,
        val password: String,
        val email: String,
    )

    data class AuthResponse(
        val token: String,
    )

    data class FavoriteRequest(
        val movieId: Int,
    )

    data class FavoriteResponse(
        val success: Boolean,
        val message: String?,
    )
}
