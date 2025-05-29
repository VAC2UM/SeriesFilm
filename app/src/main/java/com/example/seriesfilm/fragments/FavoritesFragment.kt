package com.example.seriesfilm.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesfilm.ApiClient
import com.example.seriesfilm.R
import com.example.seriesfilm.adapters.FavoritesAdapter
import com.example.seriesfilm.AuthModels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private val adapter by lazy {
        FavoritesAdapter(emptyList()) { movieId ->
            // Обработка клика по элементу
        }
    }
    private lateinit var emptyView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        // Инициализация RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadFavorites()
        return view
    }

    fun loadFavorites() {
        val token = sharedPreferences.getString("auth_token", "") ?: ""
        if (token.isEmpty()) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.authApi.getFavorites(token).enqueue(
            object : Callback<AuthModels.FavoriteResponse> {
                override fun onResponse(
                    call: Call<AuthModels.FavoriteResponse>,
                    response: Response<AuthModels.FavoriteResponse>
                ) {
                    if (response.isSuccessful) {
                        val favorites = response.body()?.favorites ?: emptyList()
                        adapter.updateFavorites(favorites)
                    } else {
                        Toast.makeText(context, "Failed to load favorites", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthModels.FavoriteResponse>, t: Throwable) {
                    Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}