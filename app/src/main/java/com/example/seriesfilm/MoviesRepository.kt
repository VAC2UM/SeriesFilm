package com.example.seriesfilm

import com.example.seriesfilm.Data.AutocompleteSearchResponse
import com.example.seriesfilm.Data.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
    private val api: Api

    init {
        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.watchmode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(Api::class.java)
    }

    fun fetchMovies(
        searchValue: String,
        searchType: Int,
        callback: (List<SearchResult>?, String?) -> Unit,
    ) {
        val call = api.autocompleteSearch(searchValue, searchType)

        call.enqueue(object : Callback<AutocompleteSearchResponse> {
            override fun onResponse(
                call: Call<AutocompleteSearchResponse>,
                response: Response<AutocompleteSearchResponse>,
            ) {
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    callback(results, null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    callback(null, "Error: ${response.message()}, Error Body: $errorBody")
                }
            }

            override fun onFailure(
                call: Call<AutocompleteSearchResponse>,
                t: Throwable,
            ) {
                callback(null, "Request failed: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
