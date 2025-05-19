package com.example.seriesfilm.data

import com.google.gson.annotations.SerializedName

data class AutocompleteSearchResponse(
    @SerializedName("results") val results: List<SearchResult>,
)
