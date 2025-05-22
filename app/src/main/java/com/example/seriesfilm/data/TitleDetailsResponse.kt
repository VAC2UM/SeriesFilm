package com.example.seriesfilm.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TitleDetailsResponse(
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("plot_overview") val overwiew: String,
    @SerializedName("type") val type: String,
    @SerializedName("year") val year: Int,
    @SerializedName("end_year") val endYear: Int?,
    @SerializedName("backdrop") val backdrop: String,
    @SerializedName("user_rating") val userRating: String,
) : Parcelable
