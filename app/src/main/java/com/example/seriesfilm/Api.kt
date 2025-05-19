package com.example.seriesfilm

import com.example.seriesfilm.data.AutocompleteSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    companion object {
        const val API_KEY = "Ni6SM1MMmNV8f3vXDzAFapR65gkGl8LEbId7rQYu"
    }

    @GET("v1/autocomplete-search/")
    fun autocompleteSearch(
        @Query("search_value") searchValue: String,
        @Query("search_type") searchType: Int,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Call<AutocompleteSearchResponse>
}
