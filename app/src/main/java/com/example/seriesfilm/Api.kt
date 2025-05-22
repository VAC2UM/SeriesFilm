package com.example.seriesfilm

import com.example.seriesfilm.data.AutocompleteSearchResponse
import com.example.seriesfilm.data.TitleDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("v1/title/{title_id}/details/")
    fun getTitleDetails(
        @Path("title_id") titleId: Long,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Call<TitleDetailsResponse>
}
