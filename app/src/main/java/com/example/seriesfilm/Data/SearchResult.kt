package com.example.seriesfilm.Data

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("name") val name: String,
    @SerializedName("relevance") val relevance: Double,
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: Long,
    @SerializedName("year") val year: Int,
    @SerializedName("result_type") val resultType: String,
    @SerializedName("tmdb_id") val tmdbId: Long,
    @SerializedName("tmdb_type") val tmdbType: String,
    @SerializedName("image_url") val imageUrl: String
)
